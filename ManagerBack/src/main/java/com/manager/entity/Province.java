package com.manager.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "province")
public class Province {

    @Id
    private Integer provinceId;

    private String province;

    private Integer people;
}
