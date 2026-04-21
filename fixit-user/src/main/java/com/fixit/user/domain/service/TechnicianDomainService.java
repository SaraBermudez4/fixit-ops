package com.fixit.user.domain.service;

import com.fixit.user.domain.enums.Role;
import com.fixit.user.domain.enums.TechnicianCategory;
import com.fixit.user.domain.enums.TechnicianStatus;
import com.fixit.user.domain.exceptions.*;
import com.fixit.user.domain.model.Technician;
import com.fixit.user.domain.util.constants.DomainConstants;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class TechnicianDomainService {

    public Technician prepareTechnicianForRegistration(Technician technician, String encodedPassword) {
        log.info("Preparing technician {} for registration with email {}", technician.getUser().getName(), technician.getUser().getEmail());
        return technician.toBuilder()
                .user(technician.getUser().toBuilder()
                        .password(encodedPassword)
                        .role(Role.TECHNICIAN)
                        .build())
                .status(TechnicianStatus.AVAILABLE)
                .taskCount(0)
                .currentPoints(0)
                .build();
    }

    public void validateUniqueness(Technician technician, boolean emailExists, boolean dniExists) {
        log.info("Validating technician uniqueness. emailExists={}, dniExists={}", emailExists, dniExists);

        if (emailExists) {
            log.warn("Technician registration validation failed: email already in use");
            throw new TechnicianAlreadyExistsException(
                    String.format(DomainConstants.TECHNICIAN_EMAIL_ALREADY_EXISTS_MESSAGE,
                            technician.getUser().getEmail())
            );
        }

        if (dniExists) {
            log.warn("Technician registration validation failed: DNI already in use");
            throw new TechnicianAlreadyExistsException(
                    String.format(DomainConstants.TECHNICIAN_DNI_ALREADY_EXISTS_MESSAGE,
                            technician.getUser().getDni())
            );
        }

        log.info("Technician uniqueness validation passed successfully");
    }

    public Technician validateTechnicianExists(Technician technicianOptional, Long id) {
        log.info("Validating existence of technician with id {}", id);

        Technician technician = Optional.ofNullable(technicianOptional).orElseThrow(() -> {
            log.warn("Technician with id {} was not found", id);
            return new TechnicianNotFoundException(
                    String.format(DomainConstants.TECHNICIAN_NOT_FOUND_MESSAGE, id)
            );
        });

        log.info("Technician with id {} exists successfully", id);
        return technician;
    }

    public void validateTechnicianCanChangeCategory(Technician technician, TechnicianCategory newCategory) {
        log.info("Validating if technician with id {} can change category from {} to {}. Current status={}, taskCount={}",
                technician.getUser().getId(),
                technician.getCategory(),
                newCategory,
                technician.getStatus(),
                technician.getTaskCount());

        if (technician.getStatus() != TechnicianStatus.AVAILABLE || technician.getTaskCount() > 0) {
            log.warn("Technician {} cannot change category because status is {} and taskCount is {}",
                    technician.getUser().getName(),
                    technician.getStatus(),
                    technician.getTaskCount());

            throw new TechnicianBusyException(
                    String.format(DomainConstants.TECHNICIAN_BUSY_MESSAGE,
                            technician.getUser().getName())
            );
        }

        if (technician.getCategory().equals(newCategory)) {
            log.warn("Technician with id {} attempted to change to the same category {}",
                    technician.getUser().getId(),
                    technician.getCategory());

            throw new TechnicianSameCategoryException(
                    String.format(DomainConstants.TECHNICIAN_SAME_CATEGORY_MESSAGE,
                            technician.getUser().getId(), technician.getCategory())
            );
        }

        log.info("Technician with id {} can change category from {} to {}",
                technician.getUser().getId(),
                technician.getCategory(),
                newCategory);
    }

    public void validateTechnicianCanBeDeleted(Technician technician) {
        log.info("Validating if technician with id {} can be deleted. Current status={}, taskCount={}",
                technician.getUser().getId(),
                technician.getStatus(),
                technician.getTaskCount());

        if (technician.getStatus() != TechnicianStatus.AVAILABLE || technician.getTaskCount() > 0) {
            log.warn("Technician with id {} cannot be deleted because status is {} and taskCount is {}",
                    technician.getUser().getId(),
                    technician.getStatus(),
                    technician.getTaskCount());

            throw new TechnicianCannotBeDeletedException(
                    String.format(DomainConstants.TECHNICIAN_CANNOT_BE_DELETED_MESSAGE,
                            technician.getUser().getId(),
                            technician.getStatus())
            );
        }

        log.info("Technician with id {} can be deleted", technician.getUser().getId());
    }
}