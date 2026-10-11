package com.cts.Backend_ExamPlanner.Services;

import com.cts.Backend_ExamPlanner.dto.AssignHallsDTO;
import com.cts.Backend_ExamPlanner.dto.ExamScheduleDTO;
import com.cts.Backend_ExamPlanner.dto.StudentConflictRequest;
import com.cts.Backend_ExamPlanner.entities.ExamSchedule;
import com.cts.Backend_ExamPlanner.entities.HallAllocation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ExamScheduleService {
    public boolean hasStudentConflict(StudentConflictRequest request);

    public List<ExamSchedule> getAllSchedules();
    public ExamSchedule saveNewSchedule(ExamScheduleDTO dto);

    public List<HallAllocation> getFreeHallsForSlot(LocalDate date, LocalTime startTime, Integer duration);

    public ExamSchedule assignHalls(AssignHallsDTO dto);

    public void deleteExamSchedule(Long id);


}
