package com.cts.Backend_ExamPlanner.controllers;

import com.cts.Backend_ExamPlanner.Services.StudentService;
import com.cts.Backend_ExamPlanner.dto.StudentDTO;
import com.cts.Backend_ExamPlanner.entities.Student;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@Validated
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public Page<StudentDTO> getStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return studentService.getPaginatedStudents(page,size);
    }

    @GetMapping("/{deptCode}/{semester}")
    public ResponseEntity<?> getStudentCountByDeptandSem(@PathVariable String deptCode,@PathVariable Integer semester) {
        long count = studentService.getCountByDeptAndSemester(deptCode,semester);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/exam/{examId}")
    public ResponseEntity<?> getStudentByDeptandSemester(@PathVariable Long examId){
        List<StudentDTO> stud=studentService.getStudentsByDeptAndSemester(examId);
        return ResponseEntity.ok(stud);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addStudent(@Valid @RequestBody StudentDTO student) {
        try{
            Student stu = studentService.createStudent(student);
            return ResponseEntity.status(HttpStatus.OK).body(stu);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateStudent(@Valid @RequestBody StudentDTO student) {
        try{
            Student stu = studentService.updateStudent(student);
            return ResponseEntity.status(HttpStatus.OK).body(stu);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable String id) {
        try{
            studentService.deleteStudent(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID not found.");
        }
    }
}
