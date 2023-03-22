package com.manager.vo.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.manager.entity.StudentEnterprise;
import com.manager.entity.UserInfo;
import lombok.Data;

@Data
public class MyStudentVO {

    public MyStudentVO(UserInfo userInfo, StudentEnterprise studentEnterprise) {
        this.studentId = userInfo.getId();
        this.studentName = userInfo.getName();
        this.studentClass = userInfo.getClassName();
        this.enterpriseName = studentEnterprise.getEnterpriseName();
        this.studentPhone = userInfo.getPhone();
    }

    @JsonProperty("id")
    String studentId;

    @JsonProperty("name")
    String studentName;

    @JsonProperty("class")
    String studentClass;

    @JsonProperty("epName")
    String enterpriseName;

    @JsonProperty("phone")
    String studentPhone;
}
