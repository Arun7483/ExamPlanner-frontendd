package com.cts.Backend_ExamPlanner.Services;

import com.cts.Backend_ExamPlanner.dto.StudentExamViewDTO;
import com.cts.Backend_ExamPlanner.entities.SeatAssignment;
import com.cts.Backend_ExamPlanner.repositories.SeatAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class StudentPortalImpl implements StudentPortalService{
    @Autowired
    private SeatAssignmentRepository seatAssignmentRepository;
    @Override
    public List<StudentExamViewDTO> getStudentSchedule(String email) {
        return seatAssignmentRepository.findUpcomingExamsForStudent(email)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    private StudentExamViewDTO convertToDTO(SeatAssignment sa) {
        StudentExamViewDTO dto = new StudentExamViewDTO();
        dto.setCourseName(sa.getHallAssignment().getExamSchedule().getCourse().getName());
        dto.setDate(sa.getHallAssignment().getExamSchedule().getExamDate());
        dto.setStartTime(sa.getHallAssignment().getExamSchedule().getStartTime());
        dto.setHallId(sa.getHallAssignment().getHall().getHallId());
        dto.setFloor(sa.getHallAssignment().getHall().getFloor());
        dto.setSeatNumber(sa.getSeatNumber());
        return dto;
    }
}
