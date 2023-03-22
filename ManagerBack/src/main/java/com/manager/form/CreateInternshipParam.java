package com.manager.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;

@Data
public class CreateInternshipParam {

    // 来自session
    private String id;

    // 以下来自前端
    private Integer status;

    private String comCity;

    private Integer comCityCode;

    @Length(max = 50)
    private String comName;

    @Length(max = 50)
    private String comAddress;

    @Length(max = 20)
    private String comContactName;

    @Length(max = 1)
    private String comContactSex;

    @Length(max = 50)
    @Email
    private String comContactEmail;

    @Length(max = 11)
    private String comContactPhone;

    @Length(max = 20)
    private String comInstructorName;

    @Length(max = 1)
    private String comInstructorSex;

    @Length(max = 50)
    @Email
    private String comInstructorEmail;

    @Length(max = 11)
    private String comInstructorPhone;

    private String offerImg;
}
