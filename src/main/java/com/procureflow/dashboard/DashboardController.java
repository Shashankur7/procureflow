package com.procureflow.dashboard;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {
    private final JdbcTemplate jdbc;
    public DashboardController(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    @GetMapping("/summary")
    DashboardSummaryResponse summary() {
        Long pending = jdbc.queryForObject("select count(*) from purchase_requests where status = 'PENDING_APPROVAL'", Long.class);
        Long approved = jdbc.queryForObject("select count(*) from purchase_requests where status = 'APPROVED'", Long.class);
        Long openOrders = jdbc.queryForObject("select count(*) from purchase_orders where status in ('DRAFT', 'SENT')", Long.class);
        Long lowStock = jdbc.queryForObject("select count(*) from products p left join stock_levels s on s.product_id = p.id where p.active = true and coalesce(s.quantity_on_hand, 0) <= p.reorder_level", Long.class);
        BigDecimal value = jdbc.queryForObject("select coalesce(sum(total_amount), 0) from purchase_orders where status in ('DRAFT', 'SENT')", BigDecimal.class);
        return new DashboardSummaryResponse(pending, approved, openOrders, lowStockProducts(lowStock), value);
    }

    private long lowStockProducts(Long value) { return value == null ? 0 : value; }
}
