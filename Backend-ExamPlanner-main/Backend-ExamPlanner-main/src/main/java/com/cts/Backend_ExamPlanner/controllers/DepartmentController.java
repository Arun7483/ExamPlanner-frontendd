package com.cts.Backend_ExamPlanner.controllers;

import com.cts.Backend_ExamPlanner.Services.DepartmentService;
import com.cts.Backend_ExamPlanner.entities.Department;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@CrossOrigin(origins = "${app.cors.allowed-origin}")
@Validated
public class DepartmentController {

    @Autowired
    private DepartmentService service;

    @GetMapping
    public List<Department> listAll() {
        return service.getAll();
    }

    @PostMapping("/add")
    public ResponseEntity<?> create(@Valid  @RequestBody Department dept) {
        try {
            Department savedDept = service.save(dept);
            // Returning CREATED (201) tells Angular the operation is done
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDept);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // --- UPDATE METHOD ---
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Department departmentDetails) {
        try {
            Department updatedDept = service.update(id, departmentDetails);
            return ResponseEntity.ok(updatedDept); // Return the object with 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    //@DeleteMapping("/{id}")
    //public ResponseEntity<?> remove(@PathVariable String id) {
        //try {
            //service.delete(id);
            //return ResponseEntity.noContent().build(); // 204 No Content is best for delete
        //} catch (Exception e) {
            //return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        //}
    //}

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();

    }

}