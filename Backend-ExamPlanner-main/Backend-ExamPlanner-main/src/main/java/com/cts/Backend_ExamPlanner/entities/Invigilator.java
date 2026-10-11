package com.cts.Backend_ExamPlanner.entities;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="invigilators")
@Data
public class Invigilator {
    @Id
    @Column(name = "id")
    private String invigilatorId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false,unique = true,length = 10)
    private String phone;

    @ManyToOne
    @JoinColumn(name = "department_code",referencedColumnName = "departmentCode",nullable = false)
    private Department department;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;



}
