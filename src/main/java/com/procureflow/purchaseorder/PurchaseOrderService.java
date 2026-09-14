package com.procureflow.purchaseorder;

import com.procureflow.purchaserequest.PurchaseRequest;
import com.procureflow.common.audit.ActivityService;
import com.procureflow.purchaserequest.PurchaseRequestRepository;
import com.procureflow.purchaserequest.PurchaseRequestStatus;
import com.procureflow.supplier.Supplier;
import com.procureflow.supplier.SupplierRepository;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Year;
import java.util.UUID;
import java.util.List;

@Service
class PurchaseOrderService {
    private final PurchaseOrderRepository orders;
    private final PurchaseRequestRepository requests;
    private final SupplierRepository suppliers;
    private final JdbcTemplate jdbcTemplate;
    private final ActivityService activity;

    PurchaseOrderService(PurchaseOrderRepository orders, PurchaseRequestRepository requests,
                         SupplierRepository suppliers, JdbcTemplate jdbcTemplate, ActivityService activity) {
        this.orders = orders;
        this.requests = requests;
        this.suppliers = suppliers;
        this.jdbcTemplate = jdbcTemplate;
        this.activity = activity;
    }

    @Transactional
    PurchaseOrderResponse create(CreatePurchaseOrder command, UUID procurementUserId) {
        PurchaseRequest request = requests.findByIdForDecision(command.purchaseRequestId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase request not found"));
        if (request.getStatus() != PurchaseRequestStatus.APPROVED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Only approved requests can become purchase orders");
        }
        if (orders.existsByPurchaseRequestId(request.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A purchase order already exists for this request");
        }
        Supplier supplier = suppliers.findById(command.supplierId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplier not found"));
        if (!supplier.isActive()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Supplier is inactive");
        }

        Long sequence = jdbcTemplate.queryForObject("select nextval('purchase_order_number_sequence')", Long.class);
        String poNumber = "PF-" + Year.now().getValue() + "-" + sequence;
        PurchaseOrder order = PurchaseOrder.create(poNumber, request.getId(), supplier.getId(),
                request.getEstimatedAmount(), request.getCurrency(), procurementUserId);
        PurchaseOrderResponse response = PurchaseOrderResponse.from(orders.save(order));
        activity.audit(procurementUserId, "PURCHASE_ORDER_CREATED", "PURCHASE_ORDER", order.getId(), order.getPoNumber());
        return response;
    }

    @Transactional(readOnly = true)
    List<PurchaseOrderResponse> list() { return orders.findAllByOrderByCreatedAtDesc().stream().map(PurchaseOrderResponse::from).toList(); }

    @Transactional
    PurchaseOrderResponse changeStatus(UUID orderId, UpdatePurchaseOrderStatus command) {
        PurchaseOrder order = orders.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase order not found"));
        PurchaseOrderStatus current = order.getStatus();
        PurchaseOrderStatus next = command.status();

        if (!PurchaseOrderWorkflow.canTransition(current, next)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Cannot change purchase order from " + current + " to " + next);
        }
        order.changeStatus(next);
        activity.audit(order.getCreatedBy(), "PURCHASE_ORDER_" + next, "PURCHASE_ORDER", order.getId(), order.getPoNumber());
        activity.notify(order.getCreatedBy(), "Purchase order updated", order.getPoNumber() + " is now " + next + ".");
        return PurchaseOrderResponse.from(order);
    }
}
