package com.cts.Backend_ExamPlanner.controllers;


import com.cts.Backend_ExamPlanner.Services.ExamScheduleService;
import com.cts.Backend_ExamPlanner.Services.HallAllocationService;
import com.cts.Backend_ExamPlanner.Services.InvigilatorService;
import com.cts.Backend_ExamPlanner.Services.SeatingService;
import com.cts.Backend_ExamPlanner.dto.*;
import com.cts.Backend_ExamPlanner.entities.ExamHallAssignment;
import com.cts.Backend_ExamPlanner.entities.ExamSchedule;
import com.cts.Backend_ExamPlanner.entities.HallAllocation;
import com.cts.Backend_ExamPlanner.entities.Invigilator;
import com.cts.Backend_ExamPlanner.repositories.ExamHallAssignmentRepository;
import com.cts.Backend_ExamPlanner.repositories.SeatAssignmentRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@CrossOrigin(origins = "${app.cors.allowed-origin}")
@RestController
@RequestMapping("/api/schedules")
public class ExamScheduleController {

    @Autowired
    private SeatingService seatingService;

    @Autowired
    private ExamHallAssignmentRepository examHallAssignmentRepository;

    @Autowired
    private SeatAssignmentRepository seatAssignmentRepository;

    @Autowired
    private ExamScheduleService examScheduleService;

    @Autowired
    private InvigilatorService invigilatorService;

    @Autowired
    private HallAllocationService hallAllocationService;
    @PostMapping("/new")
    public ResponseEntity<?> createSchedule(@Valid  @RequestBody ExamScheduleDTO dto) {
        ExamSchedule savedSchedule = examScheduleService.saveNewSchedule(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSchedule);
    }

    @PostMapping("/student_conflict")
    public ResponseEntity<?> checkStudentConflict(@Valid @RequestBody StudentConflictRequest request){
        return ResponseEntity.ok(examScheduleService.hasStudentConflict(request));
    }

    @GetMapping("/available-halls")
    public ResponseEntity<List<HallAllocation>> getAvailableHalls(
            @RequestParam LocalDate date,
            @RequestParam LocalTime startTime,
            @RequestParam Integer duration) {

        List<HallAllocation> freeHalls = examScheduleService.getFreeHallsForSlot(date, startTime, duration);
        return ResponseEntity.ok(freeHalls);
    }

    @PutMapping("/assign-resources")
    public ResponseEntity<?> AssignHallsandInvigilators(@Valid @RequestBody AssignHallsDTO dto){
        ExamSchedule examSchedule = examScheduleService.assignHalls(dto);
        return ResponseEntity.ok(examSchedule);

    }
    @GetMapping("/all")
    public ResponseEntity<List<ExamSchedule>> getAllSchedules() {
        List<ExamSchedule> schedules = examScheduleService.getAllSchedules();
        return ResponseEntity.ok(schedules);
    }

    @PostMapping("/invigilators/available")
    public ResponseEntity<List<InvigilatorRequestDTO>> getAvailableInvigilators(@Valid @RequestBody CheckInvigilatorDTO dto) {
        return ResponseEntity.ok(invigilatorService.getAvailableInvigilators(dto.getDeptCode(),dto.getExamDate(),dto.getStartTime(), dto.getDuration()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteScedule(@PathVariable Long id) {
        try {
            examScheduleService.deleteExamSchedule(id);
            return ResponseEntity.noContent().build();
        }catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/invigilator/{invId}")
    public ResponseEntity<List<InvigilatorExamViewDTO>> getInvigilatorSchedule(@PathVariable String invId) {
        List<ExamHallAssignment> assignments = examHallAssignmentRepository.findUpcomingAssignments(invId);
        List<InvigilatorExamViewDTO> response = assignments.stream().map(ha -> {
            InvigilatorExamViewDTO dto = new InvigilatorExamViewDTO();
            dto.setExamId(ha.getExamSchedule().getId());
            dto.setCourseName(ha.getExamSchedule().getCourse().getName());
            dto.setDate(ha.getExamSchedule().getExamDate());
            dto.setStartTime(ha.getExamSchedule().getStartTime());
            dto.setEndTime(ha.getExamSchedule().getEndTime());
            dto.setHallId(ha.getHall().getHallId());
            dto.setFloor(ha.getHall().getFloor());
            return dto;
        }).toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{studentId}")
    public List<StudentExamViewDTO> getStudentSchedule(@PathVariable String studentId) {
        return seatAssignmentRepository.findUpcomingExamsForStudent(studentId).stream()
                .map(sa -> {
                    StudentExamViewDTO dto = new StudentExamViewDTO();
                    dto.setCourseName(sa.getHallAssignment().getExamSchedule().getCourse().getName());
                    dto.setDate(sa.getHallAssignment().getExamSchedule().getExamDate());
                    dto.setStartTime(sa.getHallAssignment().getExamSchedule().getStartTime());
                    dto.setHallId(sa.getHallAssignment().getHall().getHallId());
                    dto.setFloor(sa.getHallAssignment().getHall().getFloor());
                    dto.setSeatNumber(sa.getSeatNumber());
                    return dto;
                }).toList();
    }

    @GetMapping("/hall-assignments/{examScheduleId}/{hallId}/students")
    public List<InvigilatorAttendanceDTO> getAttendanceList(@Valid @PathVariable String hallId,@PathVariable Long examScheduleId) {
        return seatAssignmentRepository.findByHallAssignment_Hall_HallIdAndHallAssignment_ExamSchedule_Id(hallId,examScheduleId).stream()
                .map(sa -> {
                    InvigilatorAttendanceDTO dto = new InvigilatorAttendanceDTO();
                    dto.setStudentId(sa.getStudent().getStudentID());
                    dto.setStudentName(sa.getStudent().getStudentName());
                    dto.setSeatNumber(sa.getSeatNumber());
                    dto.setDepartment(sa.getStudent().getDepartment().getName());
                    return dto;
                }).toList();
    }


}
