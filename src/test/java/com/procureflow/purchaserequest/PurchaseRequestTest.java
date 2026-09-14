package com.procureflow.purchaserequest;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PurchaseRequestTest {
    @Test
    void a_new_request_is_immediately_ready_for_approval() {
        UUID requesterId = UUID.randomUUID();

        PurchaseRequest request = PurchaseRequest.submit(
                "Laptop for new engineer", "Required for onboarding", new BigDecimal("1250.00"), "INR", requesterId);

        assertThat(request.getStatus()).isEqualTo(PurchaseRequestStatus.PENDING_APPROVAL);
        assertThat(request.getRequesterId()).isEqualTo(requesterId);
        assertThat(request.getCreatedAt()).isNotNull();
    }
}
