package com.manager.vo.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserBanOrStartVO {

    @JsonProperty("ban")
    private Boolean ban;
}
