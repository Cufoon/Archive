package com.manager.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "table_summary")
public class TableSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增
    private Integer fId;

    private String studentId;

    private String teacherId;

    private String summaryReport;

    private Integer evaluation;

    @Column(name = "t_suggestions")
    private String teacherSuggestions;

    private Timestamp submitTime;

    private Timestamp checkTime;

    private Integer status;
}
