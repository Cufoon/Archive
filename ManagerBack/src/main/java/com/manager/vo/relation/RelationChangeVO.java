package com.manager.vo.relation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RelationChangeVO {

    @JsonProperty("handle")
    private boolean handle;
}
