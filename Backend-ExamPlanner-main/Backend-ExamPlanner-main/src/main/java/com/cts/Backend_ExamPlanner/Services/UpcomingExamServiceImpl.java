package com.cts.Backend_ExamPlanner.Services;

import com.cts.Backend_ExamPlanner.entities.ExamHallAssignment;
import com.cts.Backend_ExamPlanner.entities.ExamSchedule;
import com.cts.Backend_ExamPlanner.repositories.ExamScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UpcomingExamServiceImpl implements UpcomingExamService {

    private final ExamScheduleRepository examScheduleRepository;
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ISO_DATE;
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("hh:mm a");

    public UpcomingExamServiceImpl(ExamScheduleRepository examScheduleRepository) {
        this.examScheduleRepository = examScheduleRepository;
    }

    @Override
    public List<Map<String, String>> getUpcomingExamsForEmail(String email) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        List<ExamSchedule> exams = examScheduleRepository.findUpcomingForInvigilatorEmail(email, today, now);

        List<Map<String, String>> result = new ArrayList<>(exams.size());
        for (ExamSchedule exam : exams) {
            String courseName = (exam.getCourse() != null && exam.getCourse().getName() != null)
                    ? exam.getCourse().getName()
                    : "Unknown";
            String dateStr = (exam.getExamDate() != null) ? DATE_FMT.format(exam.getExamDate()) : "";
            String timeStr = (exam.getStartTime() != null) ? TIME_FMT.format(exam.getStartTime()) : "";

            List<ExamHallAssignment> assignments = exam.getHallAssignments();
            String hallIds = (assignments == null) ? "" :
                    assignments.stream()
                            .map(a -> (a.getHall() != null) ? a.getHall().getHallId() : null)
                            .filter(Objects::nonNull)
                            .distinct()
                            .collect(Collectors.joining(","));
            String invigilatorIds = (assignments == null) ? "" :
                    assignments.stream()
                            .map(a -> (a.getInvigilator() != null) ? a.getInvigilator().getInvigilatorId() : null)
                            .filter(Objects::nonNull)
                            .distinct()
                            .collect(Collectors.joining(","));
            String students = "0";

            Map<String, String> entry = new LinkedHashMap<>();
            entry.put("examId", String.valueOf(exam.getId()));
            entry.put("course", courseName);
            entry.put("date", dateStr);
            entry.put("time", timeStr);
            entry.put("hallIds", hallIds);
            entry.put("invigilatorIds", invigilatorIds);
            entry.put("students", students);

            result.add(entry);
        }
        return result;
    }


}