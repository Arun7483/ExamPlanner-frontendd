package com.cts.Backend_ExamPlanner.Services;


import com.cts.Backend_ExamPlanner.dto.DashboardDTO;
import com.cts.Backend_ExamPlanner.repositories.ExamScheduleRepository;
import com.cts.Backend_ExamPlanner.repositories.HallAllocationRepository;
import com.cts.Backend_ExamPlanner.repositories.InvigilatorRepository;
import com.cts.Backend_ExamPlanner.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private InvigilatorRepository invigilatorRepository;

    @Autowired
    private ExamScheduleRepository examScheduleRepository;

    @Autowired
    private HallAllocationRepository hallAllocationRepository;

    public DashboardDTO getDashboardContent() {
        Long totalStudents = studentRepository.count();
        Long totalInvigilators = invigilatorRepository.count();
        Long totalExamsScheduled = examScheduleRepository.count();
        Long totalHalls = hallAllocationRepository.count();

        DashboardDTO dto = new DashboardDTO();
        dto.setTotalHalls(totalHalls);
        dto.setTotalInvigilators(totalInvigilators);
        dto.setTotalExamsScheduled(totalExamsScheduled);
        dto.setTotalStudents(totalStudents);

        return dto;
    }
}
