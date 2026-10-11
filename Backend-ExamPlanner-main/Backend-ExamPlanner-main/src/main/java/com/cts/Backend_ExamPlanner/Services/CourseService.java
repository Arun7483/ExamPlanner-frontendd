package com.cts.Backend_ExamPlanner.Services;

import com.cts.Backend_ExamPlanner.dto.CourseDTO;
import com.cts.Backend_ExamPlanner.entities.Course;
import com.cts.Backend_ExamPlanner.entities.Department;
import com.cts.Backend_ExamPlanner.repositories.CourseRepository;
import com.cts.Backend_ExamPlanner.repositories.DepartmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<CourseDTO> getAll() {

        List<Course> courses = courseRepository.findAll();

        return courses.stream().map(course -> {
            CourseDTO dto = new CourseDTO();
            dto.setId(course.getId());
            dto.setCourseName(course.getName());
            dto.setCredits(course.getCredits());
            dto.setSemester(course.getSemester());
            dto.setDepartmentCode(course.getDepartment().getDepartmentCode());
            return dto;
        }).collect(Collectors.toList());
    }

    public List<CourseDTO> getCourseByDeptandSemester(String deptCode,Integer semester){
        List<Course> courses = courseRepository.findByDepartment_DepartmentCodeAndSemester(deptCode,semester);

        return courses.stream().map(course -> {
            CourseDTO dto = new CourseDTO();
            dto.setId(course.getId());
            dto.setCourseName(course.getName());
            dto.setCredits(course.getCredits());
            dto.setSemester(course.getSemester());
            dto.setDepartmentCode(course.getDepartment().getDepartmentCode());
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public Course save(CourseDTO dto) {
        if (courseRepository.existsById(dto.getId())) {
            throw new RuntimeException("Course with ID "+dto.getId()+" already exists");
        } else {
            Department dept = departmentRepository.findByDepartmentCode(dto.getDepartmentCode())
                    .orElseThrow(() -> new RuntimeException("Dept not found: " + dto.getDepartmentCode()));
            Course course = new Course();
            course.setId(dto.getId());
            course.setName(dto.getCourseName());
            course.setCredits(dto.getCredits());
            course.setDepartment(dept);
            course.setSemester(dto.getSemester()); // Set the semester before saving
            return courseRepository.saveAndFlush(course);
        }
    }
    // Change this line in CourseService.java
    @Transactional
    public Course update(CourseDTO details) { // Add semester here
        return courseRepository.findById(details.getId()).map(existing -> {
            Department dept = departmentRepository.findByDepartmentCode(details.getDepartmentCode())
                    .orElseThrow(() -> new RuntimeException("Dept not found: " + details.getDepartmentCode()));
            existing.setName(details.getCourseName());
            existing.setCredits(details.getCredits());
            existing.setDepartment(dept);
            // This line now works because we fixed the Entity in the last step!
            existing.setSemester(details.getSemester());
            return courseRepository.saveAndFlush(existing);
        }).orElseThrow(() -> new RuntimeException("Course not found"));
    }

    public void delete(String id) {
        courseRepository.deleteById(id);
    }
}
