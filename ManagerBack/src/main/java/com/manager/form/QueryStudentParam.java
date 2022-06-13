package com.manager.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class QueryStudentParam {

    @Length(max = 12)
    private String studentId;
}
