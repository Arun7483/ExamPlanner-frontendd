
package com.cts.Backend_ExamPlanner.repositories;

import com.cts.Backend_ExamPlanner.entities.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByStudentId(String studentId);
    void deleteByStudentId(String studentId);

    List<Report> findByExamId(Long examId);


    long countByCreatedAtAfter(LocalDateTime since);

    // Fetch since a date/time (for per-day aggregation in service)
    List<Report> findByCreatedAtAfter(LocalDateTime since);

    // Count grouped by exam
    @Query("select r.examId as examId, count(r) as cnt from Report r group by r.examId order by cnt desc")
    List<Object[]> countByExam();


}
