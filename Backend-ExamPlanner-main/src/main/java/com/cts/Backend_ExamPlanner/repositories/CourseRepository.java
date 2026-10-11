package com.cts.Backend_ExamPlanner.repositories;

import com.cts.Backend_ExamPlanner.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    @Query("Select c.name from Course c where c.id=id")
    String findNameById(@Param("id") String id);

   // @Query("select size(c.students) from Course c where c.id = :id")
    //Integer getStudentCount (@Param("id") String id);

    List<Course> findByDepartment_DepartmentCodeAndSemester(String deptCode,Integer semester);
}
