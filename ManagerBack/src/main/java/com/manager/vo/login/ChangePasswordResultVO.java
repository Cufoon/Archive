package com.manager.vo.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChangePasswordResultVO {

    @JsonProperty("changed")
    private boolean changed;
}
