package com.manager.vo.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserIncreaseVO {

    @JsonProperty("increased")
    private Boolean increased;
}
