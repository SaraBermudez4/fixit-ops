package com.fixit.user.infraestructure.adapters.driven.jpa.entity;

import com.fixit.user.domain.enums.TechnicianCategory;
import com.fixit.user.domain.enums.TechnicianStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "technicians")
@Getter
@Setter
public class TechnicianEntity {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    private TechnicianCategory category;

    @Enumerated(EnumType.STRING)
    private TechnicianStatus status;

    private Integer taskCount;
    private Integer currentPoints;
}