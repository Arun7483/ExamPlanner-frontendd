package com.cts.Backend_ExamPlanner.Services;

import com.cts.Backend_ExamPlanner.dto.ReportDTO;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ReportService {

    ReportDTO create(ReportDTO dto);
    List<ReportDTO> saveBulk(List<ReportDTO> dtos);

    List<ReportDTO> getByStudent(String studentId);
    List<ReportDTO> getByExam(Long examId);
    void deleteByStudent(String studentId);

    void delete(Long id);

    List<ReportDTO> findAll();

    Page<ReportDTO> searchPaged(int page,
                                int size,
                                String sort,
                                String direction,
                                String studentId,
                                Long examId,
                                LocalDateTime from,
                                LocalDateTime to);


    Map<String, Object> summaryMetrics();

    List<Map<String, Object>> countByExam();

    List<Map<String, Object>> countPerDaySince(int days);
}