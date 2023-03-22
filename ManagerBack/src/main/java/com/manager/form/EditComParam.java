package com.manager.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;

@Data
public class EditComParam {

    private String id; // 用户id

    // 以下信息来自前端
    @Length(max = 20)
    private String name;

    @Length(max = 1)
    private String sex;

    @Length(max = 11)
    private String phone;

    @Email
    @Length(max = 50)
    private String email;

    @Length(max = 20)
    private String city;

    @Length(max = 50)
    private String comName;

    @Length(max = 50)
    private String comAddress;
}
