package com.cts.Backend_ExamPlanner.Services;

import com.cts.Backend_ExamPlanner.dto.InvigilatorRequestDTO;
import com.cts.Backend_ExamPlanner.entities.Invigilator;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface InvigilatorService {
    Invigilator createInvigilator(InvigilatorRequestDTO invigilator);
    Page<Invigilator> getPaginatedInvigilator(int page, int size);
    Invigilator updateInvigilator(InvigilatorRequestDTO invigilator);
    void deleteInvigilaor(String id);
    List<InvigilatorRequestDTO> getAvailableInvigilators(String deptCode, LocalDate date, LocalTime startTime,int durationMinutes);
}
