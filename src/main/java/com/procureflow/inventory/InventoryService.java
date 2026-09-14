package com.procureflow.inventory;

import org.springframework.http.HttpStatus;
import com.procureflow.common.audit.ActivityService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
class InventoryService {
    private final ProductRepository products;
    private final JdbcTemplate jdbc;
    private final ActivityService activity;

    InventoryService(ProductRepository products, JdbcTemplate jdbc, ActivityService activity) { this.products = products; this.jdbc = jdbc; this.activity = activity; }

    @Transactional
    ProductResponse createProduct(CreateProductRequest command) {
        Product product = Product.create(command.sku().trim().toUpperCase(), command.name().trim(), command.reorderLevel());
        return ProductResponse.from(products.save(product));
    }

    @Transactional(readOnly = true)
    List<InventoryResponse> listInventory() {
        return jdbc.query("select p.id, p.sku, p.name, p.reorder_level, coalesce(s.quantity_on_hand, 0) as quantity from products p left join stock_levels s on s.product_id = p.id where p.active = true order by p.name",
                (rs, row) -> new InventoryResponse(UUID.fromString(rs.getString("id")), rs.getString("sku"), rs.getString("name"),
                        rs.getInt("quantity"), rs.getInt("reorder_level"), rs.getInt("quantity") <= rs.getInt("reorder_level")));
    }

    @Transactional(readOnly = true)
    List<InventoryTransactionResponse> listTransactions() {
        return jdbc.query("select t.id, t.product_id, p.name, p.sku, t.transaction_type, t.quantity_delta, "
                        + "t.reference_note, t.performed_by, t.created_at from inventory_transactions t "
                        + "join products p on p.id = t.product_id order by t.created_at desc limit 50",
                (rs, row) -> new InventoryTransactionResponse(UUID.fromString(rs.getString("id")),
                        UUID.fromString(rs.getString("product_id")), rs.getString("name"), rs.getString("sku"),
                        rs.getString("transaction_type"), rs.getInt("quantity_delta"), rs.getString("reference_note"),
                        UUID.fromString(rs.getString("performed_by")), rs.getTimestamp("created_at").toInstant()));
    }

    @Transactional
    InventoryResponse receive(ReceiveStockRequest command, UUID warehouseUserId) {
        Product product = products.findById(command.productId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        if (!product.isActive()) throw new ResponseStatusException(HttpStatus.CONFLICT, "Product is inactive");

        jdbc.update("insert into stock_levels(product_id, quantity_on_hand, updated_at) values (?, ?, now()) on conflict (product_id) do update set quantity_on_hand = stock_levels.quantity_on_hand + excluded.quantity_on_hand, updated_at = now()",
                product.getId(), command.quantity());
        jdbc.update("insert into inventory_transactions(id, product_id, transaction_type, quantity_delta, reference_note, performed_by, created_at) values (?, ?, 'RECEIPT', ?, ?, ?, now())",
                UUID.randomUUID(), product.getId(), command.quantity(), command.referenceNote(), warehouseUserId);
        activity.audit(warehouseUserId, "STOCK_RECEIVED", "PRODUCT", product.getId(), command.quantity() + " units: " + command.referenceNote());
        int quantity = jdbc.queryForObject("select quantity_on_hand from stock_levels where product_id = ?", Integer.class, product.getId());
        return new InventoryResponse(product.getId(), product.getSku(), product.getName(), quantity,
                product.getReorderLevel(), quantity <= product.getReorderLevel());
    }
}
