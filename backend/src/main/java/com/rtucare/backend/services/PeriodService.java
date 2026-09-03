package com.rtucare.backend.services;

import com.rtucare.backend.DTO.request.PeriodRequestDTO;
import com.rtucare.backend.DTO.request.SymptomLogRequestDTO;
import com.rtucare.backend.DTO.response.PeriodDTO;
import com.rtucare.backend.DTO.response.SymptomLogDTO;
import com.rtucare.backend.entity.Period;
import com.rtucare.backend.entity.SymptomLog;
import com.rtucare.backend.entity.UserProfile;
import com.rtucare.backend.exception.ResourceNotFoundException;
import com.rtucare.backend.repository.PeriodRepository;
import com.rtucare.backend.repository.SymptomLogRepository;
import com.rtucare.backend.repository.UserProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PeriodService {

    private final PeriodRepository periodRepository;
    private final SymptomLogRepository symptomLogRepository;
    private final UserProfileRepository userProfileRepository;

    public PeriodService(PeriodRepository periodRepository, SymptomLogRepository symptomLogRepository,
                         UserProfileRepository userProfileRepository) {
        this.periodRepository = periodRepository;
        this.symptomLogRepository = symptomLogRepository;
        this.userProfileRepository = userProfileRepository;
    }

    public PeriodDTO createPeriod(long userId, PeriodRequestDTO dto) {
        log.info("Creating period for user id: {}", userId);
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for user id: " + userId));

        Period period = new Period();
        period.setUserProfile(userProfile);
        period.setStartDate(dto.getStartDate());
        period.setEndDate(dto.getEndDate());
        period.setFlowIntensity(dto.getFlowIntensity());
        period.setSymptoms(dto.getSymptoms() == null ? new ArrayList<>() : dto.getSymptoms());
        period.setNotes(dto.getNotes());

        PeriodDTO result = toDTO(periodRepository.save(period));
        log.info("Period created successfully with id: {}", result.getId());
        return result;
    }

    public List<PeriodDTO> getPeriods(long userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for user id: " + userId));
        return periodRepository.findByUserProfileId(userProfile.getId()).stream().map(this::toDTO).toList();
    }

    public PeriodDTO getPeriod(long id) {
        return toDTO(findPeriod(id));
    }

    public PeriodDTO updatePeriod(long id, PeriodRequestDTO dto) {
        log.info("Updating period with id: {}", id);
        Period period = findPeriod(id);

        period.setStartDate(dto.getStartDate());
        period.setEndDate(dto.getEndDate());
        period.setFlowIntensity(dto.getFlowIntensity());
        if (dto.getSymptoms() != null) {
            period.setSymptoms(dto.getSymptoms());
        }
        period.setNotes(dto.getNotes());

        return toDTO(periodRepository.save(period));
    }

    public void deletePeriod(long id) {
        log.info("Deleting period with id: {}", id);
        Period period = findPeriod(id);
        symptomLogRepository.deleteAll(symptomLogRepository.findByPeriodId(id));
        periodRepository.delete(period);
        log.info("Period deleted successfully with id: {}", id);
    }

    public SymptomLogDTO addSymptom(long periodId, SymptomLogRequestDTO dto) {
        log.info("Adding symptom to period id: {}", periodId);
        Period period = findPeriod(periodId);

        SymptomLog symptomLog = new SymptomLog();
        symptomLog.setUser(period.getUserProfile().getUser());
        symptomLog.setPeriod(period);
        symptomLog.setDate(dto.getDate());
        symptomLog.setSymptom(dto.getSymptom());
        symptomLog.setSeverity(dto.getSeverity());
        symptomLog.setMood(dto.getMood());
        symptomLog.setNotes(dto.getNotes());

        SymptomLogDTO result = toSymptomDTO(symptomLogRepository.save(symptomLog));
        log.info("Symptom added successfully with id: {}", result.getId());
        return result;
    }

    public List<SymptomLogDTO> getSymptoms(long periodId) {
        findPeriod(periodId);
        return symptomLogRepository.findByPeriodId(periodId).stream().map(this::toSymptomDTO).toList();
    }

    public SymptomLogDTO updateSymptom(long symptomId, SymptomLogRequestDTO dto) {
        log.info("Updating symptom with id: {}", symptomId);
        SymptomLog symptomLog = symptomLogRepository.findById(symptomId)
                .orElseThrow(() -> new ResourceNotFoundException("Symptom log not found with id: " + symptomId));

        symptomLog.setDate(dto.getDate());
        symptomLog.setSymptom(dto.getSymptom());
        symptomLog.setSeverity(dto.getSeverity());
        symptomLog.setMood(dto.getMood());
        symptomLog.setNotes(dto.getNotes());

        return toSymptomDTO(symptomLogRepository.save(symptomLog));
    }

    public void deleteSymptom(long symptomId) {
        if (!symptomLogRepository.existsById(symptomId)) {
            throw new ResourceNotFoundException("Symptom log not found with id: " + symptomId);
        }
        log.info("Deleting symptom with id: {}", symptomId);
        symptomLogRepository.deleteById(symptomId);
        log.info("Symptom deleted successfully with id: {}", symptomId);
    }

    private Period findPeriod(long id) {
        return periodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Period not found with id: " + id));
    }

    private PeriodDTO toDTO(Period period) {
        return new PeriodDTO(period.getId(), period.getUserProfile().getUser().getId(), period.getStartDate(), period.getEndDate(),
                period.getFlowIntensity(),
                period.getSymptoms() == null ? List.of() : period.getSymptoms(), period.getNotes());
    }

    private SymptomLogDTO toSymptomDTO(SymptomLog symptomLog) {
        return new SymptomLogDTO(symptomLog.getId(), symptomLog.getUser().getId(),
                symptomLog.getPeriod() == null ? null : symptomLog.getPeriod().getId(),
                symptomLog.getDate(), symptomLog.getSymptom(), symptomLog.getSeverity(),
                symptomLog.getMood(), symptomLog.getNotes());
    }
}
