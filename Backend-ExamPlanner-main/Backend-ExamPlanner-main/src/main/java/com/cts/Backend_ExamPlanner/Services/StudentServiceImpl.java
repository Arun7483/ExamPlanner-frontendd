package com.cts.Backend_ExamPlanner.Services;


import com.cts.Backend_ExamPlanner.dto.StudentDTO;
import com.cts.Backend_ExamPlanner.entities.Department;
import com.cts.Backend_ExamPlanner.entities.ExamSchedule;
import com.cts.Backend_ExamPlanner.entities.Student;
import com.cts.Backend_ExamPlanner.entities.User;
import com.cts.Backend_ExamPlanner.repositories.DepartmentRepository;
import com.cts.Backend_ExamPlanner.repositories.ExamScheduleRepository;
import com.cts.Backend_ExamPlanner.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentServiceImpl implements com.cts.Backend_ExamPlanner.Services.StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ExamScheduleRepository examScheduleRepository;



    @Transactional
    @Override
    public Student createStudent(StudentDTO dto) {
        if(studentRepository.existsById(dto.getStudentID())) {
            throw new RuntimeException("Student with "+ dto.getStudentID() + " already exists.");
        }else{
            Department dept = departmentRepository.findByDepartmentCode(dto.getDepartmentCode())
                    .orElseThrow(() -> new RuntimeException("Dept not found: "+dto.getDepartmentCode()));
            User newUser = new User();
            newUser.setName(dto.getStudentName());
            newUser.setEmail(dto.getStudentEmail());
            newUser.setRole("STUDENT");
            newUser.setPassword(passwordEncoder.encode("Password123"));

            Student student = new Student();
            student.setStudentID(dto.getStudentID());
            student.setStudentName(dto.getStudentName());
            student.setStudentEmail(dto.getStudentEmail());
            student.setDepartment(dept);
            student.setCurrentSemester(dto.getCurrentSemester());
            student.setUser(newUser);

            return studentRepository.save(student);
        }
    }

    @Override
    public long getCountByDeptAndSemester(String deptCode, Integer semester) {
        return studentRepository.countByDeptAndSemester(deptCode,semester);
    }

    @Override
    public List<StudentDTO> getStudentsByDeptAndSemester(Long examId) {
        ExamSchedule examSchedule = examScheduleRepository.findById(examId).orElseThrow(() -> new RuntimeException("Exam Schedule not found"));

        return studentRepository.getStudentsByDeptAndSemester(examSchedule.getDepartmentCode(),examSchedule.getSemester()).stream().map(existing -> {
            StudentDTO dto = new StudentDTO();
            dto.setStudentName(existing.getStudentName());
            dto.setStudentEmail(existing.getStudentEmail());
            dto.setStudentID(existing.getStudentID());
            dto.setCurrentSemester(existing.getCurrentSemester());
            dto.setDepartmentCode(existing.getDepartment().getDepartmentCode());
            return dto;
        }).toList();
    }


    @Override
    public Page<StudentDTO> getPaginatedStudents(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        return studentRepository.findAll(pageable).map(this::convertToDTO);
    }

    @Transactional
    @Override
    public Student updateStudent(StudentDTO dto) {
        Student existingStudent = studentRepository.findById(dto.getStudentID())
                .orElseThrow(() -> new RuntimeException("Student with ID "+ dto.getStudentID()+ " not found."));
        Department dept = departmentRepository.findByDepartmentCode(dto.getDepartmentCode())
                        .orElseThrow(() -> new RuntimeException("Dept not found: "+dto.getDepartmentCode()));

        existingStudent.setStudentName(dto.getStudentName());
        existingStudent.setStudentEmail(dto.getStudentEmail());
        existingStudent.setDepartment(dept);
        existingStudent.setCurrentSemester(dto.getCurrentSemester());
        User user = existingStudent.getUser();
        if(user != null){
            user.setName(dto.getStudentName());
            user.setEmail(dto.getStudentEmail());
        }

        return studentRepository.save(existingStudent);
    }

    private StudentDTO convertToDTO(Student student){
        StudentDTO dto = new StudentDTO();
        dto.setStudentID(student.getStudentID());
        dto.setStudentName(student.getStudentName());
        dto.setStudentEmail(student.getStudentEmail());
        dto.setDepartmentCode(student.getDepartment().getDepartmentCode());
        dto.setCurrentSemester(student.getCurrentSemester());
        return dto;
    }

    @Override
    public void deleteStudent(String studentID) {
        Student student = studentRepository.findById(studentID)
                .orElseThrow(() -> new RuntimeException("Student with ID "+ studentID + " not found"));

        studentRepository.delete(student);
    }
}
