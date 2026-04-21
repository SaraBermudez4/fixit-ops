package com.fixit.user.application.usecase;

import com.fixit.user.application.port.in.ITechnicianServicePort;
import com.fixit.user.application.port.out.IPasswordEncoderPersistencePort;
import com.fixit.user.application.port.out.ITaskFeignClientPort;
import com.fixit.user.application.port.out.ITechnicianPersistencePort;
import com.fixit.user.domain.enums.TechnicianCategory;
import com.fixit.user.domain.model.Task;
import com.fixit.user.domain.model.Technician;
import com.fixit.user.domain.model.TechnicianUpdate;
import com.fixit.user.domain.model.TechnicianWorkload;
import com.fixit.user.domain.service.TechnicianDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class TechnicianUseCase implements ITechnicianServicePort {

    private final ITechnicianPersistencePort userPersistencePort;
    private final IPasswordEncoderPersistencePort passwordEncoderPort;
    private final TechnicianDomainService technicianDomainService;
    private final ITaskFeignClientPort taskFeignClientPort;

    @Override
    public Technician createTechnician(Technician technician) {
        log.info("[CREATE] Registering new technician with email: {}", technician.getUser().getEmail());

        boolean emailExists = userPersistencePort.existsByEmail(technician.getUser().getEmail());
        boolean dniExists = userPersistencePort.existsByDni(technician.getUser().getDni());
        log.debug("[CREATE] Uniqueness validation results - emailExists={}, dniExists={}", emailExists, dniExists);

        technicianDomainService.validateUniqueness(technician, emailExists, dniExists);

        String encodedPassword = passwordEncoderPort.encodePassword(technician.getUser().getPassword());
        log.debug("[CREATE] Password encoded successfully for email: {}", technician.getUser().getEmail());

        Technician technicianToSave = technicianDomainService.prepareTechnicianForRegistration(technician, encodedPassword);
        log.debug("[CREATE] Technician prepared for persistence with role={} and status={}",
                technicianToSave.getUser().getRole(), technicianToSave.getStatus());

        Technician saved = userPersistencePort.saveTechnician(technicianToSave);
        log.info("[CREATE] Technician created successfully with ID: {}", saved.getUser().getId());
        return saved;
    }

    @Override
    public List<Technician> getAll() {
        log.info("[QUERY] Fetching all technicians");
        List<Technician> technicians = userPersistencePort.findAllTechnicians();
        log.info("[QUERY-SUCCESS] Retrieved {} technicians", technicians.size());
        return technicians;
    }

    @Override
    public List<Technician> getTechniciansByCategory(TechnicianCategory category) {
        log.info("[QUERY] Fetching technicians by category: {}", category);
        List<Technician> technicians = userPersistencePort.findTechniciansByCategory(category);
        log.info("[QUERY-SUCCESS] Retrieved {} technicians for category {}", technicians.size(), category);
        return technicians;
    }

    @Override
    @Transactional
    public Technician updateTechnician(Long id, TechnicianUpdate technicianData) {
        log.info("[UPDATE] Request to update technician operational data for ID: {}", id);

        Technician existing = userPersistencePort.findById(id);
        log.debug("[UPDATE-FLOW] Current state in DB: {}", existing);

        Technician updated = existing.toBuilder()
                .category(technicianData.getCategory() != null ? technicianData.getCategory() : existing.getCategory())
                .status(technicianData.getStatus() != null ? technicianData.getStatus() : existing.getStatus())
                .taskCount(technicianData.getTaskCount() != null ? technicianData.getTaskCount() : existing.getTaskCount())
                .currentPoints(technicianData.getCurrentPoints() != null ? technicianData.getCurrentPoints() : existing.getCurrentPoints())
                .build();

        log.debug("[UPDATE-FLOW] New state to persist: {}", updated);
        Technician result = userPersistencePort.updateTechnician(updated);
        log.info("[UPDATE-SUCCESS] Technician ID: {} updated successfully", id);
        return result;
    }

    @Override
    public TechnicianWorkload getTechnicianWorkload(Long id){
        log.info("[WORKLOAD] Calculating workload for technician ID: {}", id);

        Technician technician = technicianDomainService.validateTechnicianExists(userPersistencePort.findById(id), id);

        log.debug("[WORKLOAD-FEIGN] Calling Task Microservice for technician ID: {}", id);
        List<Task> tasks = taskFeignClientPort.findByTechnicianId(id);
        log.debug("[WORKLOAD-FEIGN] Received {} tasks from Task Microservice", tasks.size());

        TechnicianWorkload workload = TechnicianWorkload.createNew(technician, tasks);
        log.info("[WORKLOAD-SUCCESS] Workload calculated for {}: {} tasks assigned", technician.getUser().getName(), tasks.size());
        return workload;
    }

    @Override
    @Transactional
    public Technician updateTechnicianCategory(Long id, TechnicianCategory newCategory){
        log.info("[CATEGORY-CHANGE] Attempting to change category to {} for technician ID: {}", newCategory, id);

        Technician technician = userPersistencePort.findById(id);
        technicianDomainService.validateTechnicianExists(technician, id);
        technicianDomainService.validateTechnicianCanChangeCategory(technician, newCategory);

        Technician updated = technician.toBuilder()
                .category(newCategory)
                .build();

        Technician result = userPersistencePort.updateTechnician(updated);
        log.info("[CATEGORY-SUCCESS] Category updated for technician ID: {}", id);
        return result;
    }

    @Override
    public void delete(Long id) {
        log.warn("[DELETE] Request to delete technician ID: {}", id);
        Technician technician = getTechnicianById(id);
        technicianDomainService.validateTechnicianCanBeDeleted(technician);
        userPersistencePort.deleteById(id);
        log.info("[DELETE-SUCCESS] Technician ID: {} removed from system", id);
    }

    @Override
    public Technician getTechnicianById(Long id) {
        log.debug("[QUERY] Fetching technician details for ID: {}", id);
        return technicianDomainService.validateTechnicianExists(userPersistencePort.findById(id), id);
    }
}