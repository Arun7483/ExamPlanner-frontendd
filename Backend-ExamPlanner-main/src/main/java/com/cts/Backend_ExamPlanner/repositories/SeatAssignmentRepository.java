package com.cts.Backend_ExamPlanner.repositories;

import com.cts.Backend_ExamPlanner.entities.SeatAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeatAssignmentRepository extends JpaRepository<SeatAssignment, Long> {


    List<SeatAssignment> findByHallAssignment_Hall_HallIdAndHallAssignment_ExamSchedule_Id(String hallId, Long examId);
    @Query("SELECT sa FROM SeatAssignment sa " +
            "WHERE sa.student.studentEmail = :email " +
            "AND sa.hallAssignment.examSchedule.examDate >= CURRENT_DATE " +
            "ORDER BY sa.hallAssignment.examSchedule.examDate ASC")
    List<SeatAssignment> findUpcomingExamsForStudent(@Param("email") String email);

    List<SeatAssignment> findByHallAssignmentId(Long hallAssignmentId);

    boolean existsByHallAssignment_ExamSchedule_Id(Long examScheduleId);

    void deleteByHallAssignment_ExamSchedule_Id(Long examScheduleId);
}
