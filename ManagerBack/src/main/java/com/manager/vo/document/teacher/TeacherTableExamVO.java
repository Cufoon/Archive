package com.manager.vo.document.teacher;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TeacherTableExamVO {

    @JsonProperty("className")
    private String className;

    @JsonProperty("name")
    private String name;

    @JsonProperty("sid")
    private String studentId;

    @JsonProperty("tName")
    private String teacherName;

    @JsonProperty("instructorName")
    private String instructorName;

    @JsonProperty("instructorPhone")
    private String instructorPhone;

    @JsonProperty("instructorEmail")
    private String instructorEmail;

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

    @JsonProperty("attendance")
    private String attendance;

    @JsonProperty("eptSuggestions")
    private String eptSuggestions;
}
