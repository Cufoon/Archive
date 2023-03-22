package com.manager.form.document;

import lombok.Data;

@Data
public class HandleTableParam {

    private String sid;

    private Integer category;

    private Integer order;

    private TableDataParam tableData;
}
