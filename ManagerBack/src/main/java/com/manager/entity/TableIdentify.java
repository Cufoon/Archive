package com.manager.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "table_identify")
public class TableIdentify {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增
    private Integer fId;

    private String studentId;

    private String teacherId;

    private Date startDate;

    private Date endDate;

    private String project;

    private String personalSummary;

    private String attendance;

    private String eptSuggestions;

    private String epSuggestions;

    private Integer evaluation;

    @Column(name = "t_suggestions")
    private String teacherSuggestions;

    private Timestamp submitTime;

    private Timestamp checkTime;

    private String Note;

    private Integer status;
}
