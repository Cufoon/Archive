package com.manager.vo.intern;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PeriodListVO {

    @JsonProperty("currentCreated")
    private boolean currentCreated;

    @JsonProperty("periodList")
    private List<PeriodVO> periodList;
}
