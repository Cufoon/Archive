package com.manager.vo.count;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProvinceVO {

    @JsonProperty("code")
    private Integer provinceId;

    @JsonProperty("name")
    private String province;

    @JsonProperty("value")
    private Integer people;
}
