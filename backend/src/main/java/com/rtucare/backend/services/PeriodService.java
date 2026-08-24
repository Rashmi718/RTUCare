package com.rtucare.backend.services;

import com.rtucare.backend.DTO.request.PeriodRequestDTO;
import com.rtucare.backend.DTO.request.SymptomLogRequestDTO;
import com.rtucare.backend.DTO.response.PeriodDTO;
import com.rtucare.backend.DTO.response.SymptomLogDTO;
import com.rtucare.backend.entity.Period;
import com.rtucare.backend.entity.SymptomLog;
import com.rtucare.backend.entity.User;
import com.rtucare.backend.exception.ResourceNotFoundException;
import com.rtucare.backend.repository.PeriodRepository;
import com.rtucare.backend.repository.SymptomLogRepository;
import com.rtucare.backend.repository.UserRepository;
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
    private final UserRepository userRepository;

    public PeriodService(PeriodRepository periodRepository, SymptomLogRepository symptomLogRepository,
                         UserRepository userRepository) {
        this.periodRepository = periodRepository;
        this.symptomLogRepository = symptomLogRepository;
        this.userRepository = userRepository;
    }

    public PeriodDTO createPeriod(long userId, PeriodRequestDTO dto) {
        logger.info("Creating period for user id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Period period = new Period();
        period.setUser(user);
        period.setStartDate(dto.getStartDate());
        period.setEndDate(dto.getEndDate());
        period.setFlowIntensity(dto.getFlowIntensity());
        period.setSymptoms(dto.getSymptoms() == null ? new ArrayList<>() : dto.getSymptoms());
        period.setNotes(dto.getNotes());

        PeriodDTO result = toDTO(periodRepository.save(period));
        logger.info("Period created successfully with id: {}", result.getId());
        return result;
    }

    public List<PeriodDTO> getPeriods(long userId) {
        return periodRepository.findByUserId(userId).stream().map(this::toDTO).toList();
    }

    public PeriodDTO getPeriod(long id) {
        return toDTO(findPeriod(id));
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

        return toDTO(periodRepository.save(period));
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
        symptomLog.setUser(period.getUser());
        symptomLog.setPeriod(period);
        symptomLog.setDate(dto.getDate());
        symptomLog.setSymptom(dto.getSymptom());
        symptomLog.setSeverity(dto.getSeverity());
        symptomLog.setMood(dto.getMood());
        symptomLog.setNotes(dto.getNotes());

        SymptomLogDTO result = toSymptomDTO(symptomLogRepository.save(symptomLog));
        logger.info("Symptom added successfully with id: {}", result.getId());
        return result;
    }

    public List<SymptomLogDTO> getSymptoms(long periodId) {
        findPeriod(periodId);
        return symptomLogRepository.findByPeriodId(periodId).stream().map(this::toSymptomDTO).toList();
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

        return toSymptomDTO(symptomLogRepository.save(symptomLog));
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

    private PeriodDTO toDTO(Period period) {
        return new PeriodDTO(period.getId(), period.getUser().getId(), period.getStartDate(), period.getEndDate(),
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
