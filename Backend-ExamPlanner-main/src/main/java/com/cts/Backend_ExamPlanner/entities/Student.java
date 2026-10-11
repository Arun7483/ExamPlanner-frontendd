package com.cts.Backend_ExamPlanner.entities;

import com.cts.Backend_ExamPlanner.entities.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="students")
@Data
public class Student {
    @Id
    private String studentID;

    @Column(name = "name", nullable = false)
    private String studentName;

    @Column(name="email",nullable = false,unique = true)
    private String studentEmail;

    @ManyToOne
    @JoinColumn(name = "department_code", referencedColumnName = "departmentCode")
    private Department department;

    private Integer currentSemester;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;

}
