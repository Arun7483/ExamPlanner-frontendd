package com.cts.Backend_ExamPlanner.repositories;

import com.cts.Backend_ExamPlanner.dto.StudentExamViewDTO;
import com.cts.Backend_ExamPlanner.entities.SeatAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface StudentPortalRepository extends JpaRepository<SeatAssignment, Long> {

    @Query("SELECT DISTINCT new com.cts.Backend_ExamPlanner.dto.StudentExamViewDTO(" +
            "s.studentName, s.studentID, d.name, s.currentSemester, " +
            "c.name, es.examDate, es.startTime, es.endTime, h.hallId, h.floor, sa.seatNumber, es.id) " +
            "FROM SeatAssignment sa " +
            "JOIN sa.student s " +
            "JOIN s.department d " +
            "JOIN sa.hallAssignment ha " +
            "JOIN ha.hall h " +
            "JOIN ha.examSchedule es " +
            "JOIN es.course c " +
            "WHERE s.studentEmail = :email")
    List<StudentExamViewDTO> findAdmitCardDetailsByEmail(@Param("email") String email);
}