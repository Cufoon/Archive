package com.manager.vo.relation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ChooseQueryVO {

    @JsonProperty("state")
    private Integer state;

    @JsonProperty("teachers")
    private List<Map<String, String>> teachers;
}
