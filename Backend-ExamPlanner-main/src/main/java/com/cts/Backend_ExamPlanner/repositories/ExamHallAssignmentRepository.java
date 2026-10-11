package com.cts.Backend_ExamPlanner.repositories;

import com.cts.Backend_ExamPlanner.entities.ExamHallAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExamHallAssignmentRepository extends JpaRepository<ExamHallAssignment, Long> {

    @Query("SELECT ha FROM ExamHallAssignment ha " +
            "WHERE ha.invigilator.invigilatorId = :invId " +
            "AND ha.examSchedule.examDate >= CURRENT_DATE " +
            "ORDER BY ha.examSchedule.examDate ASC, ha.examSchedule.startTime ASC")
    List<ExamHallAssignment> findUpcomingAssignments(@Param("invId") String invId);
}
