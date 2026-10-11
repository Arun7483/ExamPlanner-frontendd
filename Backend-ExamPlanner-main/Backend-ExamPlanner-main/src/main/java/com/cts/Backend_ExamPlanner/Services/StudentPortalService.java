package com.cts.Backend_ExamPlanner.Services;

import com.cts.Backend_ExamPlanner.dto.StudentExamViewDTO;

import java.util.List;

public interface StudentPortalService {
   public List<StudentExamViewDTO> getStudentSchedule(String email);
}
