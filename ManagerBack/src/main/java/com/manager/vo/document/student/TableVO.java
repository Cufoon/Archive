package com.manager.vo.document.student;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TableVO {

    @JsonProperty("name")
    private String fileName;

    @JsonProperty("sid")
    private String studentId;

    @JsonProperty("category")
    private Integer category;

    @JsonProperty("order")
    private Integer order;

    @JsonProperty("submitTime")
    private String submitTime;

    @JsonProperty("checkTime")
    private String checkTime;

    @JsonProperty("evaluation")
    private Integer evaluation;

    @JsonProperty("deadline")
    private String deadline;

    @JsonProperty("due")
    private Boolean due;

    @JsonProperty("status")
    private Integer status;
}
