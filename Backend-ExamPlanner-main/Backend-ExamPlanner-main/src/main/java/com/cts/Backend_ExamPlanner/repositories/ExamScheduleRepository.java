package com.cts.Backend_ExamPlanner.repositories;

import com.cts.Backend_ExamPlanner.entities.ExamSchedule;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ExamScheduleRepository extends JpaRepository<ExamSchedule, Long> {

    @Query("""
           SELECT COUNT(e) > 0
           FROM ExamSchedule e
           WHERE e.departmentCode = :deptCode
             AND e.semester = :sem
             AND e.examDate = :date
             AND (:startTime < e.endTime AND :endTime > e.startTime)
           """)
    boolean existsStudentConflict(
            @Param("deptCode") String deptCode,
            @Param("sem") Integer sem,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    @EntityGraph(attributePaths = {
            "course",
            "hallAssignments",
            "hallAssignments.hall",
            "hallAssignments.invigilator"
    })
    @Query("""
           select distinct es
           from ExamSchedule es
           join es.hallAssignments a
           where a.invigilator.email = :email
             and (
                    es.examDate > :today
                 or (es.examDate = :today and es.endTime >= :nowTime)
                 )
           order by es.examDate asc, es.startTime asc
           """)
    List<ExamSchedule> findUpcomingForInvigilatorEmail(@Param("email") String email,
                                                       @Param("today") LocalDate today,
                                                       @Param("nowTime") LocalTime nowTime);
}