package com.manager.service.document;

import com.manager.form.document.EditDocParam;
import com.sun.istack.NotNull;

public interface EditAllService {

    /**
     * editAll
     * 根据学生ID，表单编号，表单月次返回对应的表单的基础信息
     */
    Object editAll(@NotNull EditDocParam editDocParam);
}
