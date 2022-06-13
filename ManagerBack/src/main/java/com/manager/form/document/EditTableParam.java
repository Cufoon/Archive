package com.manager.form.document;

import lombok.Data;

@Data
public class EditTableParam {

    //用户 id
    private String id;

    // 以下来自前端
    private Integer category;

    private Integer order;

    private FormDataParam formData;
}
