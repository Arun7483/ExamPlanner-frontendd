package com.cts.Backend_ExamPlanner.Services;

import com.cts.Backend_ExamPlanner.dto.StudentDTO;
import com.cts.Backend_ExamPlanner.entities.Student;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StudentService {

    public Student createStudent(StudentDTO dto);

    public long getCountByDeptAndSemester(String deptCode,Integer semester);

    public List<StudentDTO> getStudentsByDeptAndSemester(Long examId);

    public Page<StudentDTO> getPaginatedStudents(int page, int size);

    public Student updateStudent(StudentDTO dto);

    public void deleteStudent(String studentID);
}
