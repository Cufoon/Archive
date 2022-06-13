package com.manager.vo.enterprise;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InternshipVO {

    @JsonProperty("state")
    private String state;

    @JsonProperty("comCity")
    private String comCity;

    @JsonProperty("comName")
    private String comName;

    @JsonProperty("comAddress")
    private String comAddress;

    @JsonProperty("comContact")
    private String comContact;

    @JsonProperty("sex")
    private String sex;

    @JsonProperty("comContactEmail")
    private String comContactEmail;

    @JsonProperty("comContactPhone")
    private String comContactPhone;

    @JsonProperty("comInstructorName")
    private String comInstructorName;

    @JsonProperty("comInstructorSex")
    private String comInstructorSex;

    @JsonProperty("comInstructorEmail")
    private String comInstructorEmail;

    @JsonProperty("comInstructorPhone")
    private String comInstructorPhone;

    @JsonProperty("offerImg")
    private String offerImg;
}
