package com.rtucare.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "medical_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id" , nullable = false , unique = true)
    private User user;

    @Column(nullable = false)
    private Boolean smoker;

    @Column(nullable = false)
    private Boolean alcoholConsumer;

    @ElementCollection
    @CollectionTable(name = "user_medical_history", joinColumns = @JoinColumn(name = "medical_history_id"))
    @Column(name = "disease", nullable = false)
    private List<String> diseases;

    @OneToMany(mappedBy = "medicalHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Medication> medications;
}
