package com.manager.service.document;

import com.manager.form.document.EditTableParam;
import com.manager.vo.document.student.EditTableVO;
import com.sun.istack.NotNull;

public interface EditTableService {

    /**
     * editTable
     * 根据学生ID、表单编号、表单月次和表单数据保存学生所填写的表单内容，返回保存是否成功
     */
    EditTableVO editTable(@NotNull EditTableParam editTableParam, int status);
}
