package com.procureflow.purchaserequest;

import com.procureflow.common.audit.ActivityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
class PurchaseRequestService {
    private final PurchaseRequestRepository repository;
    private final PurchaseRequestDecisionRepository decisionRepository;
    private final ActivityService activity;

    PurchaseRequestService(PurchaseRequestRepository repository, PurchaseRequestDecisionRepository decisionRepository,
                           ActivityService activity) {
        this.repository = repository;
        this.decisionRepository = decisionRepository;
        this.activity = activity;
    }

    @Transactional
    PurchaseRequestResponse create(CreatePurchaseRequest command, UUID requesterId) {
        PurchaseRequest request = PurchaseRequest.submit(command.title().trim(), command.description(),
                command.estimatedAmount(), command.currency().toUpperCase(Locale.ROOT), requesterId);
        PurchaseRequestResponse response = PurchaseRequestResponse.from(repository.save(request));
        activity.audit(requesterId, "REQUEST_SUBMITTED", "PURCHASE_REQUEST", request.getId(), request.getTitle());
        return response;
    }

    @Transactional(readOnly = true)
    List<PurchaseRequestResponse> findForRequester(UUID requesterId) {
        return repository.findByRequesterIdOrderByCreatedAtDesc(requesterId).stream()
                .map(PurchaseRequestResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    List<PurchaseRequestResponse> findPending() {
        return repository.findByStatusOrderByCreatedAtAsc(PurchaseRequestStatus.PENDING_APPROVAL).stream()
                .map(PurchaseRequestResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    List<PurchaseRequestResponse> findApproved() {
        return repository.findByStatusOrderByCreatedAtAsc(PurchaseRequestStatus.APPROVED).stream()
                .map(PurchaseRequestResponse::from)
                .toList();
    }

    @Transactional
    PurchaseRequestDecisionResponse decide(UUID requestId, DecidePurchaseRequest command, UUID managerId) {
        PurchaseRequest request = repository.findByIdForDecision(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase request not found"));

        PurchaseRequestStatus status = command.decision() == DecidePurchaseRequest.Decision.APPROVE
                ? PurchaseRequestStatus.APPROVED : PurchaseRequestStatus.REJECTED;
        try {
            request.decide(status);
        } catch (IllegalStateException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, exception.getMessage());
        }
        PurchaseRequestDecision decision = PurchaseRequestDecision.record(
                request.getId(), managerId, status, command.comment());
        PurchaseRequestDecisionResponse response = PurchaseRequestDecisionResponse.from(decisionRepository.save(decision));
        activity.audit(managerId, "REQUEST_" + status, "PURCHASE_REQUEST", request.getId(), command.comment());
        activity.notify(request.getRequesterId(), "Purchase request " + status.name().toLowerCase(),
                request.getTitle() + " was " + status.name().toLowerCase() + ".");
        return response;
    }
}
