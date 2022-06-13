package com.manager.vo.count;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CityVO {

    private Integer cityId;

    private String city;

    private Integer provinceId;

    private Integer people;
}
