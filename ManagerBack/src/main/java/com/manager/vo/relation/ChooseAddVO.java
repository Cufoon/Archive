package com.manager.vo.relation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChooseAddVO {

    @JsonProperty("succeeded")
    private boolean succeeded;
}