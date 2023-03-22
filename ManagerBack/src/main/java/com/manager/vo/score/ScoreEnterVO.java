package com.manager.vo.score;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ScoreEnterVO {

    @JsonProperty("entered")
    private Boolean entered;
}
