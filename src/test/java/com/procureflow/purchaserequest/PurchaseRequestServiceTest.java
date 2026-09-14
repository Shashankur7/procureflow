package com.procureflow.purchaserequest;

import com.procureflow.common.audit.ActivityService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PurchaseRequestServiceTest {
    @Test
    void manager_approval_changes_request_status_and_records_decision() {
        PurchaseRequestRepository requests = mock(PurchaseRequestRepository.class);
        PurchaseRequestDecisionRepository decisions = mock(PurchaseRequestDecisionRepository.class);
        PurchaseRequestService service = new PurchaseRequestService(requests, decisions, mock(ActivityService.class));
        UUID requestId = UUID.randomUUID();
        UUID managerId = UUID.randomUUID();
        PurchaseRequest request = PurchaseRequest.submit("Laptop", "Onboarding", new BigDecimal("65000"), "INR", UUID.randomUUID());

        when(requests.findByIdForDecision(request.getId())).thenReturn(Optional.of(request));
        when(decisions.save(any(PurchaseRequestDecision.class))).thenAnswer(call -> call.getArgument(0));

        PurchaseRequestDecisionResponse result = service.decide(request.getId(),
                new DecidePurchaseRequest(DecidePurchaseRequest.Decision.APPROVE, "Approved"), managerId);

        assertThat(request.getStatus()).isEqualTo(PurchaseRequestStatus.APPROVED);
        assertThat(result.decision()).isEqualTo("APPROVED");
        assertThat(result.decidedBy()).isEqualTo(managerId);
    }
}
