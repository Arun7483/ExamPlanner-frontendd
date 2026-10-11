package com.cts.Backend_ExamPlanner.controllers;

import com.cts.Backend_ExamPlanner.dto.CourseDTO;
import com.cts.Backend_ExamPlanner.entities.Course;
import com.cts.Backend_ExamPlanner.Services.CourseService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "${app.cors.allowed-origin:http://localhost:4200}")
@Validated
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping
    public List<CourseDTO> listAll() {
        return courseService.getAll();
    }

    @GetMapping("/{deptCode}/{semester}")
    public List<CourseDTO> getCoursesByDeptandSemster(@PathVariable String deptCode, @PathVariable Integer semester) {
        return courseService.getCourseByDeptandSemester(deptCode,semester);
    }

    @PostMapping("/add")
    public ResponseEntity<?> create(@Valid @RequestBody CourseDTO course) {
        try {
            Course c = courseService.save(course);
            return ResponseEntity.status(HttpStatus.OK).body(c);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody CourseDTO course) {
        try {
            Course c = courseService.update(course);
            return ResponseEntity.status(HttpStatus.OK).body(c);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remove(@PathVariable String id) {
        courseService.delete(id);
        return ResponseEntity.ok().build();
    }
}
