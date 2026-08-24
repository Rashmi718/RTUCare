package com.rtucare.backend.services;

import com.rtucare.backend.DTO.request.MedicalHistoryRequestDTO;
import com.rtucare.backend.DTO.request.MedicationRequestDTO;
import com.rtucare.backend.DTO.response.MedicalHistoryResponseDTO;
import com.rtucare.backend.DTO.response.MedicationResponseDTO;
import com.rtucare.backend.entity.MedicalHistory;
import com.rtucare.backend.entity.Medication;
import com.rtucare.backend.entity.User;
import com.rtucare.backend.exception.DuplicateResourceException;
import com.rtucare.backend.exception.ResourceNotFoundException;
import com.rtucare.backend.repository.MedicalHistoryRepository;
import com.rtucare.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MedicalHistoryService {

    private static final Logger logger = LoggerFactory.getLogger(MedicalHistoryService.class);

    private final MedicalHistoryRepository medicalHistoryRepository;
    private final UserRepository userRepository;

    public MedicalHistoryService(MedicalHistoryRepository medicalHistoryRepository, UserRepository userRepository) {
        this.medicalHistoryRepository = medicalHistoryRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public MedicalHistoryResponseDTO createMedicalHistory(long userId, MedicalHistoryRequestDTO dto) {
        logger.info("Creating medical history for user id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (medicalHistoryRepository.findByUserId(userId).isPresent()) {
            throw new DuplicateResourceException("Medical history already exists for user id: " + userId);
        }

        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setUser(user);
        medicalHistory.setSmoker(dto.getSmoker());
        medicalHistory.setAlcoholConsumer(dto.getAlcoholConsumer());
        medicalHistory.setDiseases(dto.getDiseases() == null ? new ArrayList<>() : dto.getDiseases());
        medicalHistory.setMedications(toMedications(dto.getMedications(), medicalHistory));

        MedicalHistoryResponseDTO result = toDTO(medicalHistoryRepository.save(medicalHistory));
        logger.info("Medical history created successfully with id: {}", result.getId());
        return result;
    }

    public MedicalHistoryResponseDTO getMedicalHistory(long userId) {
        MedicalHistory medicalHistory = medicalHistoryRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Medical history not found for user id: " + userId));
        return toDTO(medicalHistory);
    }

    @Transactional
    public MedicalHistoryResponseDTO updateMedicalHistory(long userId, MedicalHistoryRequestDTO dto) {
        logger.info("Updating medical history for user id: {}", userId);
        MedicalHistory medicalHistory = medicalHistoryRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Medical history not found for user id: " + userId));

        medicalHistory.setSmoker(dto.getSmoker());
        medicalHistory.setAlcoholConsumer(dto.getAlcoholConsumer());

        if (dto.getDiseases() != null) {
            medicalHistory.setDiseases(dto.getDiseases());
        }

        if (dto.getMedications() != null) {
            List<Medication> current = medicalHistory.getMedications();
            if (current == null) {
                medicalHistory.setMedications(new ArrayList<>());
                current = medicalHistory.getMedications();
            }
            current.clear();
            current.addAll(toMedications(dto.getMedications(), medicalHistory));
        }

        MedicalHistoryResponseDTO result = toDTO(medicalHistoryRepository.save(medicalHistory));
        logger.info("Medical history updated successfully with id: {}", result.getId());
        return result;
    }

    private List<Medication> toMedications(List<MedicationRequestDTO> medicationDTOs, MedicalHistory medicalHistory) {
        if (medicationDTOs == null) {
            return new ArrayList<>();
        }
        return medicationDTOs.stream().map(m -> {
            Medication medication = new Medication();
            medication.setName(m.getName());
            medication.setDosage(m.getDosage());
            medication.setDurationMonths(m.getDurationMonths());
            medication.setMedicalHistory(medicalHistory);
            return medication;
        }).toList();
    }

    public MedicalHistoryResponseDTO toDTO(MedicalHistory medicalHistory) {
        List<MedicationResponseDTO> medications = medicalHistory.getMedications() == null ? List.of()
                : medicalHistory.getMedications().stream().map(m ->
                        new MedicationResponseDTO(m.getId(), m.getMedicalHistory().getId(), m.getName(), m.getDosage(), m.getDurationMonths()))
                .toList();
        return new MedicalHistoryResponseDTO(medicalHistory.getId(), medicalHistory.getUser().getId(),
                medicalHistory.getSmoker(), medicalHistory.getAlcoholConsumer(),
                medicalHistory.getDiseases() == null ? List.of() : medicalHistory.getDiseases(), medications);
    }
}
