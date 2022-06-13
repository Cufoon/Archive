package com.manager.vo.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LogoutResultVO {

    @JsonProperty("logout")
    private Boolean logout;
}
