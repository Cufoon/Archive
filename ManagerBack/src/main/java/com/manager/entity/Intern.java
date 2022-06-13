package com.manager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
public class Intern {

    @Id
    private String periodId;

    private Date fromDate;

    private Date endDate;

    private String periodName;

    public Intern() {
    }
}
