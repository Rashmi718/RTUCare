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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PeriodService {

    private static final Logger logger = LoggerFactory.getLogger(PeriodService.class);

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
        logger.info("Creating period for user id: {}", userId);
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for user id: " + userId));

        Period period = new Period();
        period.setUserProfile(userProfile);
        period.setStartDate(dto.getStartDate());
        period.setEndDate(dto.getEndDate());
        period.setFlowIntensity(dto.getFlowIntensity());
        period.setSymptoms(dto.getSymptoms() == null ? new ArrayList<>() : dto.getSymptoms());
        period.setNotes(dto.getNotes());

        PeriodDTO result = PeriodDTO.from(periodRepository.save(period));
        logger.info("Period created successfully with id: {}", result.getId());
        return result;
    }

    public List<PeriodDTO> getPeriods(long userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for user id: " + userId));
        return periodRepository.findByUserProfileId(userProfile.getId()).stream().map(PeriodDTO::from).toList();
    }

    public PeriodDTO getPeriod(long id) {
        return PeriodDTO.from(findPeriod(id));
    }

    public PeriodDTO updatePeriod(long id, PeriodRequestDTO dto) {
        logger.info("Updating period with id: {}", id);
        Period period = findPeriod(id);

        period.setStartDate(dto.getStartDate());
        period.setEndDate(dto.getEndDate());
        period.setFlowIntensity(dto.getFlowIntensity());
        if (dto.getSymptoms() != null) {
            period.setSymptoms(dto.getSymptoms());
        }
        period.setNotes(dto.getNotes());

        return PeriodDTO.from(periodRepository.save(period));
    }

    public void deletePeriod(long id) {
        logger.info("Deleting period with id: {}", id);
        Period period = findPeriod(id);
        symptomLogRepository.deleteAll(symptomLogRepository.findByPeriodId(id));
        periodRepository.delete(period);
        logger.info("Period deleted successfully with id: {}", id);
    }

    public SymptomLogDTO addSymptom(long periodId, SymptomLogRequestDTO dto) {
        logger.info("Adding symptom to period id: {}", periodId);
        Period period = findPeriod(periodId);

        SymptomLog symptomLog = new SymptomLog();
        symptomLog.setUser(period.getUserProfile().getUser());
        symptomLog.setPeriod(period);
        symptomLog.setDate(dto.getDate());
        symptomLog.setSymptom(dto.getSymptom());
        symptomLog.setSeverity(dto.getSeverity());
        symptomLog.setMood(dto.getMood());
        symptomLog.setNotes(dto.getNotes());

        SymptomLogDTO result = SymptomLogDTO.from(symptomLogRepository.save(symptomLog));
        logger.info("Symptom added successfully with id: {}", result.getId());
        return result;
    }

    public List<SymptomLogDTO> getSymptoms(long periodId) {
        findPeriod(periodId);
        return symptomLogRepository.findByPeriodId(periodId).stream().map(SymptomLogDTO::from).toList();
    }

    public SymptomLogDTO updateSymptom(long symptomId, SymptomLogRequestDTO dto) {
        logger.info("Updating symptom with id: {}", symptomId);
        SymptomLog symptomLog = symptomLogRepository.findById(symptomId)
                .orElseThrow(() -> new ResourceNotFoundException("Symptom log not found with id: " + symptomId));

        symptomLog.setDate(dto.getDate());
        symptomLog.setSymptom(dto.getSymptom());
        symptomLog.setSeverity(dto.getSeverity());
        symptomLog.setMood(dto.getMood());
        symptomLog.setNotes(dto.getNotes());

        return SymptomLogDTO.from(symptomLogRepository.save(symptomLog));
    }

    public void deleteSymptom(long symptomId) {
        if (!symptomLogRepository.existsById(symptomId)) {
            throw new ResourceNotFoundException("Symptom log not found with id: " + symptomId);
        }
        logger.info("Deleting symptom with id: {}", symptomId);
        symptomLogRepository.deleteById(symptomId);
        logger.info("Symptom deleted successfully with id: {}", symptomId);
    }

    private Period findPeriod(long id) {
        return periodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Period not found with id: " + id));
    }
}
