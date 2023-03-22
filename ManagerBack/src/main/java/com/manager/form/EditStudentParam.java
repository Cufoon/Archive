package com.manager.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;

@Data
public class EditStudentParam {

    private String id;

    // 以下来自前端
    @Length(max = 20)
    private String studentName;

    @Length(max = 1)
    private String sex;

    @Length(max = 10)
    private String className;

    @Length(max = 11)
    private String phoneNumber;

    @Length(max = 50)
    @Email
    private String email;

    @Length(max = 500)
    private String introduction;

    private String avatar;
}
