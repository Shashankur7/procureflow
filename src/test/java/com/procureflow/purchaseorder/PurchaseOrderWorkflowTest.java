package com.procureflow.purchaseorder;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PurchaseOrderWorkflowTest {
    @Test
    void allows_only_valid_purchase_order_state_transitions() {
        assertThat(PurchaseOrderWorkflow.canTransition(PurchaseOrderStatus.DRAFT, PurchaseOrderStatus.SENT)).isTrue();
        assertThat(PurchaseOrderWorkflow.canTransition(PurchaseOrderStatus.SENT, PurchaseOrderStatus.RECEIVED)).isTrue();
        assertThat(PurchaseOrderWorkflow.canTransition(PurchaseOrderStatus.DRAFT, PurchaseOrderStatus.RECEIVED)).isFalse();
        assertThat(PurchaseOrderWorkflow.canTransition(PurchaseOrderStatus.RECEIVED, PurchaseOrderStatus.SENT)).isFalse();
    }
}
