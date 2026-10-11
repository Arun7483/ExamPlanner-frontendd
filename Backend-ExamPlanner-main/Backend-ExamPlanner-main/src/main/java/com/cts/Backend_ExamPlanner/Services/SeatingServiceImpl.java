package com.cts.Backend_ExamPlanner.Services;

import com.cts.Backend_ExamPlanner.entities.ExamHallAssignment;
import com.cts.Backend_ExamPlanner.entities.ExamSchedule;
import com.cts.Backend_ExamPlanner.entities.SeatAssignment;
import com.cts.Backend_ExamPlanner.entities.Student;
import com.cts.Backend_ExamPlanner.repositories.SeatAssignmentRepository;
import com.cts.Backend_ExamPlanner.repositories.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatingServiceImpl implements SeatingService{

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SeatAssignmentRepository seatAssignmentRepository;

    @Transactional
    @Override
    public void generateSeating(ExamSchedule schedule) {
        if(seatAssignmentRepository.existsByHallAssignment_ExamSchedule_Id(schedule.getId())) {
            return;
        }

        List<Student> students = studentRepository.findByDepartment_DepartmentCodeAndCurrentSemester(schedule.getCourse().getDepartment().getDepartmentCode(),schedule.getSemester());
        int studentIndex = 0;
        for(ExamHallAssignment ha : schedule.getHallAssignments()) {
            int capacity = ha.getHall().getCapacity();

            for(int i=1;i<=capacity && studentIndex < students.size(); i++) {
                Student student = students.get(studentIndex++);

                SeatAssignment seat = new SeatAssignment();
                seat.setHallAssignment(ha);
                seat.setStudent(student);
                seat.setSeatNumber(ha.getHall().getHallId()+ "-"+String.format("%03d", i));
                seatAssignmentRepository.save(seat);
            }
        }

        if(studentIndex < students.size()) {
            throw new RuntimeException("Not enough seats for all students in " + schedule.getCourse().getId() + " for semester " + schedule.getSemester());
        }
    }
}
