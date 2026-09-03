package com.rtucare.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "habits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Habits {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_profile_id", nullable = false, unique = true)
    private UserProfile userProfile;

    @Column(nullable = false)
    private Boolean smoker;

    @Column(nullable = false)
    private Boolean alcoholConsumer;

    @Column(nullable = false)
    private Boolean goodSleep;

    @Column(nullable = false)
    private Boolean balancedDiet;

    @Column(nullable = false)
    private Boolean exercise;

    @Column(nullable = false)
    private Boolean consumeExcessiveDrugs;
}
