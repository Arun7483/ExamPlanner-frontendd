package com.cts.Backend_ExamPlanner.repositories;

import com.cts.Backend_ExamPlanner.entities.Invigilator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface InvigilatorRepository extends JpaRepository<Invigilator,String> {
    @Query("SELECT i FROM Invigilator i WHERE i.department.departmentCode <> :deptCode " +
            "AND i.id NOT IN (" +
            "    SELECT ha.invigilator.id FROM ExamHallAssignment ha " +
            "    WHERE ha.examSchedule.examDate = :date " +
            "    AND (:startTime < ha.examSchedule.endTime AND :endTime > ha.examSchedule.startTime)" +
            ")")
    List<Invigilator> findAvailableInvigilators(
            @Param("deptCode") String deptCode,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);
}
