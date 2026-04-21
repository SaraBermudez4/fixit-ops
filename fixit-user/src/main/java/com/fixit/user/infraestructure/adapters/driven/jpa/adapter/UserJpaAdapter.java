package com.fixit.user.infraestructure.adapters.driven.jpa.adapter;

import com.fixit.user.application.port.out.ITechnicianPersistencePort;
import com.fixit.user.domain.enums.TechnicianCategory;
import com.fixit.user.domain.model.Technician;
import com.fixit.user.domain.model.User;
import com.fixit.user.infraestructure.adapters.driven.jpa.entity.TechnicianEntity;
import com.fixit.user.infraestructure.adapters.driven.jpa.entity.UserEntity;
import com.fixit.user.infraestructure.adapters.driven.jpa.mapper.IUserEntityMapper;
import com.fixit.user.infraestructure.adapters.driven.jpa.repository.ITechnicianRepository;
import com.fixit.user.infraestructure.adapters.driven.jpa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserJpaAdapter implements ITechnicianPersistencePort {

    private final IUserRepository userRepository;
    private final ITechnicianRepository technicianRepository;
    private final IUserEntityMapper mapper;

    @Override
    @Transactional
    public Technician saveTechnician(Technician technician) {
        UserEntity userEntity = mapper.toUserEntity(technician.getUser());
        UserEntity savedUser = userRepository.save(userEntity);

        TechnicianEntity techEntity = mapper.toTechEntity(technician);

        techEntity.setUser(savedUser);

        TechnicianEntity savedTech = technicianRepository.save(techEntity);

        return mapper.toTechDomain(savedTech);
    }

    @Override
    @Transactional
    public Technician updateTechnician(Technician technician) {
        TechnicianEntity techEntity = technicianRepository.findById(technician.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Technician not found with ID: " + technician.getUser().getId()));

        System.out.println("Sincronizando entidad gestionada ID: " + techEntity.getId());

        techEntity.setCategory(technician.getCategory());
        techEntity.setStatus(technician.getStatus());
        techEntity.setTaskCount(technician.getTaskCount());
        techEntity.setCurrentPoints(technician.getCurrentPoints());

        TechnicianEntity savedTech = technicianRepository.save(techEntity);

        return mapper.toTechDomain(savedTech);
    }

    @Override
    public void deleteById(Long id) {
        technicianRepository.deleteById(id);
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByDni(String dni) {
        return userRepository.existsByDni(dni);
    }

    @Override
    public User findByEmail(String email) {
        return mapper.toUserDomain(userRepository.findByEmail(email));
    }

    @Override
    public List<Technician> findAllTechnicians() {
        return technicianRepository.findAll().stream()
                .map(mapper::toTechDomain)
                .toList();
    }

    @Override
    public List<Technician> findTechniciansByCategory(TechnicianCategory category) {
        return technicianRepository.findByCategory(category).stream()
                .map(mapper::toTechDomain)
                .toList();
    }

    @Override
    public Technician findById(Long id) {
        return technicianRepository.findById(id)
                .map(mapper::toTechDomain)
                .orElse(null);
    }
}