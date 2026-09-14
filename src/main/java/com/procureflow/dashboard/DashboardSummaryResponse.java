package com.procureflow.dashboard;

import java.math.BigDecimal;

public record DashboardSummaryResponse(long pendingApprovals, long approvedRequests, long openPurchaseOrders,
                                       long lowStockProducts, BigDecimal openPurchaseOrderValue) { }
