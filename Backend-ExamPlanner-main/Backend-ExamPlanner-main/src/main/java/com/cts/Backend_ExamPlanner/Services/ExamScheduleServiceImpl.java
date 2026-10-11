package com.cts.Backend_ExamPlanner.Services;

import com.cts.Backend_ExamPlanner.dto.AssignHallsDTO;
import com.cts.Backend_ExamPlanner.dto.ExamScheduleDTO;
import com.cts.Backend_ExamPlanner.dto.StudentConflictRequest;
import com.cts.Backend_ExamPlanner.entities.*;
import com.cts.Backend_ExamPlanner.repositories.CourseRepository;
import com.cts.Backend_ExamPlanner.repositories.ExamScheduleRepository;
import com.cts.Backend_ExamPlanner.repositories.HallAllocationRepository;
import com.cts.Backend_ExamPlanner.repositories.InvigilatorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ExamScheduleServiceImpl implements ExamScheduleService{

    @Autowired
    private SeatingService seatingService;

    @Autowired
    private ExamScheduleRepository examScheduleRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private HallAllocationRepository hallAllocationRepository;

    @Autowired
    private InvigilatorRepository invigilatorRepository;


    @Override
    public boolean hasStudentConflict(StudentConflictRequest request) {
        LocalTime proposedStartTime = request.getStartTime().minusMinutes(20);
        LocalTime proposedEndTime = request.getStartTime().plusMinutes(request.getDuration() + 20);

        return examScheduleRepository.existsStudentConflict(
                request.getDepartmentCode(),
                request.getSemester(),
                request.getExamDate(),
                proposedStartTime,
                proposedEndTime
        );
    }

    @Override
    public List<ExamSchedule> getAllSchedules() {
        return examScheduleRepository.findAll();
    }

    @Transactional
    @Override
    public ExamSchedule saveNewSchedule(ExamScheduleDTO dto) {
        StudentConflictRequest request = new StudentConflictRequest();
        request.setDepartmentCode(dto.getDepartmentCode());
        request.setExamDate(dto.getExamDate());
        request.setStartTime(dto.getStartTime());
        request.setSemester(dto.getSemester());
        request.setDuration(dto.getDuration());
        if(hasStudentConflict(request)) {
            throw new RuntimeException("Cannot save: Student conflict detected for this time slot");

        }
        Course course = courseRepository.findById(dto.getCourseID())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        ExamSchedule schedule = new ExamSchedule();
        schedule.setCourse(course);
        schedule.setExamDate(dto.getExamDate());
        schedule.setStartTime(dto.getStartTime());

        LocalTime endTime = request.getStartTime().plusMinutes(dto.getDuration());
        schedule.setEndTime(endTime);
        schedule.setDurationMinutes(dto.getDuration());
        schedule.setDepartmentCode(dto.getDepartmentCode());
        schedule.setSemester(dto.getSemester());

        return examScheduleRepository.save(schedule);
    }

    @Override
    public List<HallAllocation> getFreeHallsForSlot(LocalDate date, LocalTime startTime, Integer duration) {
        LocalTime bufferedStart= startTime.minusMinutes(20);
        LocalTime bufferedEnd=startTime.plusMinutes(duration+20);
        return hallAllocationRepository.findAvailableHalls(date,bufferedStart,bufferedEnd);
    }


    @Transactional
    @Override
    public ExamSchedule assignHalls(AssignHallsDTO dto) {
        ExamSchedule existingSchedule =  examScheduleRepository.findById(dto.getExamId())
                .orElseThrow(() -> new RuntimeException("Exam schedule not found with ID: "+dto.getExamId()));
        existingSchedule.getHallAssignments().clear();
        List<ExamHallAssignment> newAssignments = dto.getAssignments().stream().map(pair -> {
            HallAllocation hall = hallAllocationRepository.findById(pair.getHallId())
                    .orElseThrow(() -> new RuntimeException("Hall not found: "+pair.getHallId()));

            Invigilator invigilator = invigilatorRepository.findById(pair.getInvigilatorId())
                    .orElseThrow(() -> new RuntimeException("Invigilator not found: "+pair.getInvigilatorId()));

            ExamHallAssignment assignment = new ExamHallAssignment();
            assignment.setExamSchedule(existingSchedule);
            assignment.setHall(hall);
            assignment.setInvigilator(invigilator);
            return assignment;
        }).toList();

        existingSchedule.getHallAssignments().addAll(newAssignments);
        seatingService.generateSeating(existingSchedule);
        return examScheduleRepository.save(existingSchedule);
    }


    @Transactional
    @Override
    public void deleteExamSchedule(Long id) {
        if(!examScheduleRepository.existsById(id)) {
            throw new RuntimeException("Exam schedule not found: "+id);
        }
        examScheduleRepository.deleteById(id);
    }


}
