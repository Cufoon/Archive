package com.manager.vo.relation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.manager.entity.StudentTeacherRelation;
import com.manager.entity.UserInfo;
import lombok.Data;

import java.util.Date;

@Data
public class StudentApplyInfoVO {

    public StudentApplyInfoVO(UserInfo userInfo, StudentTeacherRelation studentTeacherRelation) {
        this.name = userInfo.getName();
        this.id = studentTeacherRelation.getStudentId();
        this.studentClass = userInfo.getClassName();
        this.applyTime = studentTeacherRelation.getSendTime();
    }

    private String name;

    private String id;

    @JsonProperty("class")
    private String studentClass;

    private Date applyTime;
}
