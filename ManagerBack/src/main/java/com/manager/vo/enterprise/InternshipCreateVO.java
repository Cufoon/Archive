package com.manager.vo.enterprise;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InternshipCreateVO {

    @JsonProperty("edited")
    private boolean edited;
}
