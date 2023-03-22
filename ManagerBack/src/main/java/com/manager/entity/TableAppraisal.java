package com.manager.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "table_appraisal")
public class TableAppraisal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增
    private Integer fId;

    private String studentId;

    private String teacherId;

    private Date startDate;

    private Date endDate;

    private String project;

    @Column(name = "ept_evaluation1")
    private Integer enterpriseTeacherEvaluation1;

    @Column(name = "ept_evaluation2")
    private Integer enterpriseTeacherEvaluation2;

    @Column(name = "ept_evaluation3")
    private Integer enterpriseTeacherEvaluation3;

    @Column(name = "ept_evaluation4")
    private Integer enterpriseTeacherEvaluation4;

    @Column(name = "ept_evaluation5")
    private Integer enterpriseTeacherEvaluation5;

    @Column(name = "ept_evaluation6")
    private Integer enterpriseTeacherEvaluation6;

    @Column(name = "ept_evaluation7")
    private Integer enterpriseTeacherEvaluation7;

    @Column(name = "ept_evaluation8")
    private Integer enterpriseTeacherEvaluation8;

    @Column(name = "ept_evaluation9")
    private Integer enterpriseTeacherEvaluation9;

    @Column(name = "ept_suggestions")
    private String enterpriseTeacherSuggestions;

    @Column(name = "t_evaluation1")
    private Integer teacherEvaluation1;

    @Column(name = "t_evaluation2")
    private Integer teacherEvaluation2;

    @Column(name = "t_evaluation3")
    private Integer teacherEvaluation3;

    private Integer evaluation;

    @Column(name = "t_suggestions")
    private String teacherSuggestions;

    private Timestamp submitTime;

    private Timestamp checkTime;

    private Integer status;
}
