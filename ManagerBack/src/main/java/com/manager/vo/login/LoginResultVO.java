package com.manager.vo.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResultVO {

    @JsonProperty("logined")
    private Boolean logined;

    @JsonProperty("type")
    private Integer type;
}
