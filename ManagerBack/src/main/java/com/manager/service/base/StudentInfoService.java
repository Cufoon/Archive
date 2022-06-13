package com.manager.service.base;

import com.manager.form.EditStudentParam;
import com.manager.vo.base.StudentInfoEditVO;
import com.manager.vo.base.StudentInfoQueryVO;
import com.sun.istack.NotNull;

public interface StudentInfoService {

    /**
     * getStudentInfo
     * 根据学生ID返回学生的个人信息
     */
    StudentInfoQueryVO getStudentInfo(@NotNull String studentId);

    /**
     * editStudentInfo
     * 根据前端数据修改学生的个人信息，返回修改是否成功
     */
    StudentInfoEditVO editStudentInfo(@NotNull EditStudentParam param);
}
