package com.manager.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "student_teacher_relation")
public class StudentTeacherRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增
    private Integer rId;

    private String studentId;

    private String teacherId;

    private Integer state;

    private Date sendTime;

    private Date dealTime;
}
