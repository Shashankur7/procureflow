package com.procureflow.purchaseorder;

final class PurchaseOrderWorkflow {
    private PurchaseOrderWorkflow() { }

    static boolean canTransition(PurchaseOrderStatus current, PurchaseOrderStatus next) {
        return (current == PurchaseOrderStatus.DRAFT && (next == PurchaseOrderStatus.SENT || next == PurchaseOrderStatus.CANCELLED))
                || (current == PurchaseOrderStatus.SENT && (next == PurchaseOrderStatus.RECEIVED || next == PurchaseOrderStatus.CANCELLED));
    }
}
