package com.cts.Backend_ExamPlanner.repositories;

import com.cts.Backend_ExamPlanner.entities.HallAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@Repository
public interface HallAllocationRepository extends JpaRepository<HallAllocation, String>{
    @Query("SELECT h FROM HallAllocation h " +
            "WHERE h.availability = com.cts.Backend_ExamPlanner.entities.Availability.YES " +
            "AND h.hallId NOT IN (" +
            "    SELECT ha.hall.hallId FROM ExamHallAssignment ha " +
            "    WHERE ha.examSchedule.examDate = :date " +
            "    AND (:startTime < ha.examSchedule.endTime AND :endTime > ha.examSchedule.startTime)" +
            ")")
    List<HallAllocation> findAvailableHalls(
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}
