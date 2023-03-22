package com.manager.vo.document.student;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LookTableSummaryVO {

    @JsonProperty("className")
    private String className;

    @JsonProperty("name")
    private String name;

    @JsonProperty("sid")
    private String studentId;

    @JsonProperty("phone")
    private String phone;

    // 以上学生、教师复用，以下教师使用
    @JsonProperty("summaryReport")
    private String summaryReport;

    @JsonProperty("evaluation")
    private Integer evaluation;

    @JsonProperty("teacherSuggestion")
    private String teacherSuggestion;
}
