package com.rtucare.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "symptom_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SymptomLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "period_id")
    private Period period;

    @Column(nullable = false)
    private LocalDate date;

    private String symptom;
    private Integer severity;

    private String mood;

    @Column(length = 2000)
    private String notes;
}
