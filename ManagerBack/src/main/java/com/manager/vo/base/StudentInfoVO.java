package com.manager.vo.base;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentInfoVO {

    public StudentInfoVO() {
    }

    private String studentName;

    private String sex;

    private String className;

    private String phoneNumber;

    private String email;

    private String introduction;

    private String avatar;

    private String studentId;
}
