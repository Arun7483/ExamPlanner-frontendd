package com.cts.Backend_ExamPlanner.repositories;

import com.cts.Backend_ExamPlanner.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    @Query("SELECT COUNT(s) FROM Student s WHERE s.department.departmentCode = :deptCode AND s.currentSemester = :semester")
    long countByDeptAndSemester(@Param("deptCode") String deptCode,@Param("semester") Integer semester);

    @Query("SELECT s FROM Student s WHERE s.department.departmentCode = :deptCode AND s.currentSemester = :semester")
    List<Student> getStudentsByDeptAndSemester(@Param("deptCode") String deptCode,@Param("semester") Integer semester);

    List<Student> findByDepartment_DepartmentCodeAndCurrentSemester(String deptCode, Integer semester);
    Student findByStudentEmail(String studentEmail);
}

