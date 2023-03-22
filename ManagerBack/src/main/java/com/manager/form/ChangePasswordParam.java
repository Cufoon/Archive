package com.manager.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ChangePasswordParam {

    @JsonProperty("oldPassword")
    @Length(max = 16)
    private String oldPassword;

    @JsonProperty("newPassword")
    @Length(max = 16, min = 8)
    private String newPassword;
}
