package com.manager.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class StudentScore {

    @Id
    private String studentId;

    private Integer reportScore1;

    private Integer reportScore2;

    private Integer reportScore3;

    private Integer examScore1;

    private Integer examScore2;

    private Integer examScore3;

    private Integer identifyScore;

    private Integer appraisalScore;

    private Integer summaryScore;

    private Double groupScore;

    private Double sumScore;
}
