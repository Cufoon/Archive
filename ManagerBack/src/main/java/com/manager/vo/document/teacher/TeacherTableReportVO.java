package com.manager.vo.document.teacher;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TeacherTableReportVO {

    @JsonProperty("className")
    private String className;

    @JsonProperty("name")
    private String name;

    @JsonProperty("sid")
    private String studentId;

    @JsonProperty("tName")
    private String teacherName;

    @JsonProperty("epName")
    private String epName;

    @JsonProperty("epCity")
    private String epCity;

    @JsonProperty("instructorName")
    private String instructorName;

    @JsonProperty("instructorPhone")
    private String instructorPhone;

    @JsonProperty("instructorEmail")
    private String instructorEmail;

    // 以上学生、教师复用，以下教师使用
    @JsonProperty("startDate")
    private String startDate;

    @JsonProperty("endDate")
    private String endDate;

    @JsonProperty("projectName")
    private String project;

    @JsonProperty("livingAddress")
    private String livingAddress;

    @JsonProperty("livingPhone")
    private String livingPhone;

    @JsonProperty("livingCondition")
    private String livingCondition;

    @JsonProperty("jobContent")
    private String jobContent;

    @JsonProperty("requirements")
    private String requirements;
}
