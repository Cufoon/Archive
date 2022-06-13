package com.manager.vo.document.student;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EditTableVO {

    @JsonProperty("edited")
    private Boolean edited;
}
