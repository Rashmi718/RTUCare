package com.rtucare.backend.entity;

import com.rtucare.backend.enums.HeightUnit;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private Double heightCm;

    @Enumerated(EnumType.STRING)
    private HeightUnit heightUnit;

    private Double weightKg;
    private Integer age;

    // manually or map
    private String location;
}
