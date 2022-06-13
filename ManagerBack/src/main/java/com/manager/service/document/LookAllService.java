package com.manager.service.document;

import com.sun.istack.NotNull;

public interface LookAllService {

    /**
     * lookAll
     * 根据学生ID、表单编号和表单月次返回获取对应表单的详细信息
     */
    Object lookAll(@NotNull String studentId, @NotNull Integer category, @NotNull Integer order);
}
