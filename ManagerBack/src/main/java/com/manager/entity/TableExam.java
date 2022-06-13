package com.manager.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "table_exam")
public class TableExam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增
    private Integer fId;

    private String studentId;

    private String teacherId;

    private Integer orderNum;

    private Date startDate;

    private Date endDate;

    private String project;

    private String attendance;

    private String eptSuggestions;

    private Integer evaluation;

    @Column(name = "t_suggestions")
    private String teacherSuggestions;

    private Timestamp submitTime;

    private Timestamp checkTime;

    private Integer status;
}
