package com.manager.vo.document.teacher;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HandleTableVO {

    @JsonProperty("handled")
    private Boolean handled;
}
