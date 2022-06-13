package com.manager.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "city")
public class City {

    @Id
    private Integer cityId;

    private String city;

    private Integer provinceId;

    private Integer people;
}
