package com.manager.vo.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.manager.entity.StudentEnterprise;
import com.manager.entity.UserInfo;
import lombok.Data;

@Data
public class QueryStudentVO {

    public QueryStudentVO(UserInfo userInfo, StudentEnterprise se) {
        name = userInfo.getName();
        id = userInfo.getId();
        clazz = userInfo.getClassName();
        sex = userInfo.getSex();
        phone = userInfo.getPhone();
        email = userInfo.getEmail();
        epName = se.getEnterpriseName();
        epCity = se.getEnterpriseCity();
        eptName = se.getInstructorName();
        eptEmail = se.getInstructorEmail();
        eptPhone = se.getInstructorPhone();
    }

    private String name;

    private String id;

    @JsonProperty("class")
    private String clazz;

    private String sex;

    private String phone;

    private String email;

    private String epName;

    private String epCity;

    private String eptName;

    private String eptEmail;

    private String eptPhone;

    @JsonProperty("avatar")
    private String avatar;
}
