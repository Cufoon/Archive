package com.manager.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "deadline")
public class Deadline {

    @Id
    private String studentId;

    private Date startDate;

    private Date reportDeadline1;

    private Date reportDeadline2;

    private Date reportDeadline3;

    private Date examDeadline1;

    private Date examDeadline2;

    private Date examDeadline3;

    private Date identifyDeadline;

    private Date appraisalDeadline;

    private Date summaryDeadline;
}
