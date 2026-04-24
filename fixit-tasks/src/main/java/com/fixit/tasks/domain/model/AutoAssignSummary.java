package com.fixit.tasks.domain.model;

import com.fixit.tasks.domain.util.constants.DomainConstants;
import lombok.Builder;

@Builder
public record AutoAssignSummary(
                long assignedCount,
                long remainingPendingCount,
                boolean success,
                String message) {



}
