package com.manager.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
public class LoginParam {

    @Length(max = 12)
    private String username;

    @Length(max = 20)
    private String password;
}
