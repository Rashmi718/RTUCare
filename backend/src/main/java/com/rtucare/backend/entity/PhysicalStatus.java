package com.rtucare.backend.entity;

import com.rtucare.backend.enums.HeightUnit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "physical_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhysicalStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_profile_id", nullable = false, unique = true)
    private UserProfile userProfile;

    private Double heightCm;

    @Enumerated(EnumType.STRING)
    private HeightUnit heightUnit;

    private Double weightKg;
    private Integer age;

    private String location;
}
