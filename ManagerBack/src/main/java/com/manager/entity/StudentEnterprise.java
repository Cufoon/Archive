package com.manager.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "student_enterprise")
public class StudentEnterprise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增
    private Integer eId;

    private String studentId;

    @Column(name = "ep_name")
    private String enterpriseName;

    @Column(name = "ep_city")
    private String enterpriseCity;

    @Column(name = "ep_address")
    private String enterpriseAddress;

    private String instructorName;

    private String instructorSex;

    private String instructorEmail;

    private String instructorPhone;

    private String contactName;

    private String contactSex;

    private String contactEmail;

    private String contactPhone;

    private String offerImg;

    private String archive;
}
