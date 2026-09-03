package com.rtucare.backend.services;

import com.rtucare.backend.DTO.request.DiseaseRequestDTO;
import com.rtucare.backend.DTO.request.MedicalHistoryRequestDTO;
import com.rtucare.backend.DTO.request.MedicationRequestDTO;
import com.rtucare.backend.DTO.response.DiseaseResponseDTO;
import com.rtucare.backend.DTO.response.MedicalHistoryResponseDTO;
import com.rtucare.backend.DTO.response.MedicationResponseDTO;
import com.rtucare.backend.entity.Disease;
import com.rtucare.backend.entity.MedicalHistory;
import com.rtucare.backend.entity.Medication;
import com.rtucare.backend.entity.UserProfile;
import com.rtucare.backend.exception.DuplicateResourceException;
import com.rtucare.backend.exception.ResourceNotFoundException;
import com.rtucare.backend.repository.MedicalHistoryRepository;
import com.rtucare.backend.repository.UserProfileRepository;
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
    private final UserProfileRepository userProfileRepository;

    public MedicalHistoryService(MedicalHistoryRepository medicalHistoryRepository, UserProfileRepository userProfileRepository) {
        this.medicalHistoryRepository = medicalHistoryRepository;
        this.userProfileRepository = userProfileRepository;
    }

    @Transactional
    public MedicalHistoryResponseDTO createMedicalHistory(long userId, MedicalHistoryRequestDTO dto) {
        logger.info("Creating medical history for user id: {}", userId);
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for user id: " + userId));

        if (medicalHistoryRepository.findByUserProfileUserId(userId).isPresent()) {
            throw new DuplicateResourceException("Medical history already exists for user id: " + userId);
        }

        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setUserProfile(userProfile);
        medicalHistory.setHasMedicalIssues(dto.getHasMedicalIssues());

        if (Boolean.TRUE.equals(dto.getHasMedicalIssues())) {
            medicalHistory.setDiseases(toDiseases(dto.getDiseases(), medicalHistory));
            medicalHistory.setMedications(toMedications(dto.getMedications(), medicalHistory));
        }

        MedicalHistoryResponseDTO result = toDTO(medicalHistoryRepository.save(medicalHistory));
        logger.info("Medical history created successfully with id: {}", result.getId());
        return result;
    }

    public MedicalHistoryResponseDTO getMedicalHistory(long userId) {
        MedicalHistory medicalHistory = medicalHistoryRepository.findByUserProfileUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Medical history not found for user id: " + userId));
        return toDTO(medicalHistory);
    }

    @Transactional
    public MedicalHistoryResponseDTO updateMedicalHistory(long userId, MedicalHistoryRequestDTO dto) {
        logger.info("Updating medical history for user id: {}", userId);
        MedicalHistory medicalHistory = medicalHistoryRepository.findByUserProfileUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Medical history not found for user id: " + userId));

        medicalHistory.setHasMedicalIssues(dto.getHasMedicalIssues());

        if (Boolean.TRUE.equals(dto.getHasMedicalIssues())) {
            if (dto.getDiseases() != null) {
                List<Disease> currentDiseases = medicalHistory.getDiseases();
                if (currentDiseases == null) {
                    medicalHistory.setDiseases(new ArrayList<>());
                    currentDiseases = medicalHistory.getDiseases();
                }
                currentDiseases.clear();
                currentDiseases.addAll(toDiseases(dto.getDiseases(), medicalHistory));
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
        } else {
            medicalHistory.setDiseases(null);
            medicalHistory.setMedications(null);
        }

        MedicalHistoryResponseDTO result = toDTO(medicalHistoryRepository.save(medicalHistory));
        logger.info("Medical history updated successfully with id: {}", result.getId());
        return result;
    }

    private List<Disease> toDiseases(List<DiseaseRequestDTO> diseaseDTOs, MedicalHistory medicalHistory) {
        if (diseaseDTOs == null) {
            return new ArrayList<>();
        }
        return diseaseDTOs.stream().map(d -> {
            Disease disease = new Disease();
            disease.setMedicalHistory(medicalHistory);
            disease.setName(d.getName());
            disease.setDiagnoseDate(d.getDiagnoseDate());
            disease.setStartDate(d.getStartDate());
            disease.setEndDate(d.getEndDate());
            disease.setStillOngoing(d.getStillOngoing());
            if (d.getMedication() != null) {
                disease.setMedication(toMedication(d.getMedication(), medicalHistory));
            }
            return disease;
        }).toList();
    }

    private List<Medication> toMedications(List<MedicationRequestDTO> medicationDTOs, MedicalHistory medicalHistory) {
        if (medicationDTOs == null) {
            return new ArrayList<>();
        }
        return medicationDTOs.stream().map(m -> toMedication(m, medicalHistory)).toList();
    }

    private Medication toMedication(MedicationRequestDTO m, MedicalHistory medicalHistory) {
        Medication medication = new Medication();
        medication.setName(m.getName());
        medication.setDosage(m.getDosage());
        medication.setDurationMonths(m.getDurationMonths());
        medication.setIsOngoing(m.getIsOngoing());
        medication.setMedicalHistory(medicalHistory);
        return medication;
    }

    public MedicalHistoryResponseDTO toDTO(MedicalHistory medicalHistory) {
        List<DiseaseResponseDTO> diseases = medicalHistory.getDiseases() == null ? List.of()
                : medicalHistory.getDiseases().stream().map(this::toDiseaseDTO).toList();
        List<MedicationResponseDTO> medications = medicalHistory.getMedications() == null ? List.of()
                : medicalHistory.getMedications().stream().map(m ->
                        new MedicationResponseDTO(m.getId(), m.getMedicalHistory().getId(), m.getName(), m.getDosage(), m.getDurationMonths(), m.getIsOngoing()))
                .toList();
        return new MedicalHistoryResponseDTO(medicalHistory.getId(), medicalHistory.getUserProfile().getUser().getId(),
                medicalHistory.getHasMedicalIssues(), diseases, medications);
    }

    private DiseaseResponseDTO toDiseaseDTO(Disease disease) {
        MedicationResponseDTO medicationDTO = null;
        if (disease.getMedication() != null) {
            Medication m = disease.getMedication();
            medicationDTO = new MedicationResponseDTO(m.getId(), m.getMedicalHistory().getId(),
                    m.getName(), m.getDosage(), m.getDurationMonths(), m.getIsOngoing());
        }
        return new DiseaseResponseDTO(disease.getId(), disease.getMedicalHistory().getId(),
                disease.getName(), disease.getDiagnoseDate(), disease.getStartDate(),
                disease.getEndDate(), disease.getStillOngoing(), medicationDTO);
    }
}
