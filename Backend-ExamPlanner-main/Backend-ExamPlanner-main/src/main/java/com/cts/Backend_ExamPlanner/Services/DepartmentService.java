package com.cts.Backend_ExamPlanner.Services;

import com.cts.Backend_ExamPlanner.entities.Department;
import com.cts.Backend_ExamPlanner.repositories.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository repository;

    public List<Department> getAll() {
        return repository.findAll();
    }

    public Department save(Department dept) {
        if (dept.getDepartmentId() == null || dept.getDepartmentId().trim().isEmpty()) {
            throw new RuntimeException("Department ID is required.");
        }

        if(repository.existsById(dept.getDepartmentId())) {
            throw new RuntimeException("Department ID " + dept.getDepartmentId() + " already exists!");
        }

        if(repository.findByDepartmentCode(dept.getDepartmentCode()).isPresent()) {
            throw new RuntimeException("Short Code " + dept.getDepartmentCode() + " is already taken!");
        }

        return repository.save(dept);
    }

    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Cannot delete: Department not found.");
        }
        repository.deleteById(id);
    }


    public Department update(String departmentId, Department details) {
        return repository.findById(departmentId).map(existing -> {
            // If the short code is being changed, check if the NEW code is already taken
            if (!existing.getDepartmentCode().equalsIgnoreCase(details.getDepartmentCode())) {
                if (repository.findByDepartmentCode(details.getDepartmentCode()).isPresent()) {
                    throw new RuntimeException("Short Code " + details.getDepartmentCode() + " is already in use.");
                }
            }

            existing.setName(details.getName());
            existing.setDepartmentCode(details.getDepartmentCode());
            existing.setHeadOfDepartment(details.getHeadOfDepartment());

            // Use the renamed property from your Entity (semesters plural)
            // This represents the total duration of the department (e.g., 8)
            existing.setTotalSemesters(details.getTotalSemesters());

            return repository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Department not found"));
    }

}
