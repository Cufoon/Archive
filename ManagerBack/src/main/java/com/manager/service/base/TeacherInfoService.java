package com.manager.service.base;

import com.manager.form.EditTeacherParam;
import com.manager.vo.base.TeacherInfoEditVO;
import com.manager.vo.base.TeacherInfoQueryVO;
import com.sun.istack.NotNull;

public interface TeacherInfoService {

    /**
     * getTeacherInfo
     * 根据教师ID返回教师的个人信息
     */
    TeacherInfoQueryVO getTeacherInfo(@NotNull String teacherId);

    /**
     * editTeacherInfo
     * 根据前端数据修改学生的个人信息，返回修改是否成功
     */
    TeacherInfoEditVO editTeacherInfo(@NotNull EditTeacherParam param);
}
