package com.manager.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;

@Data
public class EditTeacherParam {

    private String id; // from session

    @Length(max = 20)
    private String name;

    @Email
    @Length(max = 50)
    private String email;

    @Length(max = 11)
    private String phone;

    @Length(max = 500)
    private String introduction;

    private String avatar;
}
