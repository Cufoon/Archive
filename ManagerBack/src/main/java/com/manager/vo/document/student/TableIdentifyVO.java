package com.manager.vo.document.student;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TableIdentifyVO {

    @JsonProperty("className")
    private String className;

    @JsonProperty("name")
    private String name;

    @JsonProperty("sex")
    private String sex;

    @JsonProperty("sid")
    private String studentId;

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

    @JsonProperty("personalSummary")
    private String personalSummary;

    @JsonProperty("attendance")
    private String attendance;

    @JsonProperty("eptSuggestions")
    private String eptSuggestions;

    @JsonProperty("epSuggestions")
    private String epSuggestions;
}
