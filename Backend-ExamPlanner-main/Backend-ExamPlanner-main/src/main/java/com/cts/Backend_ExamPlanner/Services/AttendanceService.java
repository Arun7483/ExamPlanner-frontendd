package com.cts.Backend_ExamPlanner.Services;
import com.cts.Backend_ExamPlanner.entities.Student;
import com.cts.Backend_ExamPlanner.repositories.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.cts.Backend_ExamPlanner.dto.AttendanceRequestDTO;
import com.cts.Backend_ExamPlanner.dto.AttendanceResponseDTO;
import com.cts.Backend_ExamPlanner.repositories.AttendanceRepository;
import com.cts.Backend_ExamPlanner.entities.Attendance;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceService {
    @Autowired
    private AttendanceRepository repository;

    @Autowired
    private StudentRepository studentRepository;

    @Transactional
    public void saveBulkAttendance(AttendanceRequestDTO dto) {
        Long examId = dto.getExamId();
        String hallId = dto.getHallId();

        repository.deleteByExamIdAndHallId(examId,hallId);

        List<Attendance> entities = dto.getStudents().stream().map(s -> {
            return Attendance.builder()
                    .examId(examId)
                    .hallId(hallId)
                    .studentId(s.getId())
                    .present(s.isPresent())
                    .build();
        }).toList();

        repository.saveAll(entities);
    }

    public List<AttendanceResponseDTO> getAttendanceByExamAndHall(Long examId, String hallId) {
        List<Attendance> attendanceList = repository.findByExamIdAndHallId(examId, hallId);

        return attendanceList.stream().map(a -> {
            String studentName = studentRepository.findById(a.getStudentId())
                    .map(Student::getStudentName)
                    .orElse("Unknown");
            return new AttendanceResponseDTO(
                    a.getId(),
                    a.getStudentId(),
                    studentName,
                    a.getPresent(),
                    a.getMarkedAt()
            );
        }).collect(Collectors.toList());
    }
}