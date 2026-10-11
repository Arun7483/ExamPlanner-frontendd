package com.cts.Backend_ExamPlanner.Services;

import com.cts.Backend_ExamPlanner.entities.ExamSchedule;

public interface SeatingService {
    public void generateSeating(ExamSchedule schedule);
}
