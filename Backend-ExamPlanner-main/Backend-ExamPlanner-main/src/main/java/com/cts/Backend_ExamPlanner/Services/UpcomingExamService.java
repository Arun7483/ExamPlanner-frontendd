package com.cts.Backend_ExamPlanner.Services;

import java.util.List;
import java.util.Map;

public interface UpcomingExamService {

    List<Map<String, String>> getUpcomingExamsForEmail(String email);
}
