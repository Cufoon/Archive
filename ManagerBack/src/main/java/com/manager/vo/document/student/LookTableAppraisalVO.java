package com.manager.vo.document.student;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LookTableAppraisalVO {

    @JsonProperty("name")
    private String name;

    @JsonProperty("sid")
    private String studentId;

    @JsonProperty("instructorName")
    private String instructorName;

    @JsonProperty("instructorPhone")
    private String instructorPhone;

    @JsonProperty("epName")
    private String epName;

    @JsonProperty("epCity")
    private String epCity;

    // 以上学生、教师复用，以下教师使用
    @JsonProperty("startDate")
    private String startDate;

    @JsonProperty("endDate")
    private String endDate;

    @JsonProperty("projectName")
    private String project;

    @JsonProperty("eptEvaluation1")
    private Integer enterpriseTeacherEvaluation1;

    @JsonProperty("eptEvaluation2")
    private Integer enterpriseTeacherEvaluation2;

    @JsonProperty("eptEvaluation3")
    private Integer enterpriseTeacherEvaluation3;

    @JsonProperty("eptEvaluation4")
    private Integer enterpriseTeacherEvaluation4;

    @JsonProperty("eptEvaluation5")
    private Integer enterpriseTeacherEvaluation5;

    @JsonProperty("eptEvaluation6")
    private Integer enterpriseTeacherEvaluation6;

    @JsonProperty("eptEvaluation7")
    private Integer enterpriseTeacherEvaluation7;

    @JsonProperty("eptEvaluation8")
    private Integer enterpriseTeacherEvaluation8;

    @JsonProperty("eptEvaluation9")
    private Integer enterpriseTeacherEvaluation9;

    @JsonProperty("eptSuggestions")
    private String enterpriseTeacherSuggestions;

    @JsonProperty("teacherEvaluation1")
    private Integer teacherEvaluation1;

    @JsonProperty("teacherEvaluation2")
    private Integer teacherEvaluation2;

    @JsonProperty("teacherEvaluation3")
    private Integer teacherEvaluation3;

    @JsonProperty("evaluation")
    private Integer evaluation;

    @JsonProperty("teacherSuggestion")
    private String teacherSuggestion;
}
