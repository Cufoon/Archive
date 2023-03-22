package com.manager.service.relation;

import com.manager.form.EditComParam;
import com.manager.vo.enterprise.ComInstructorQueryVO;
import com.manager.vo.relation.ChooseQueryVO;
import com.manager.vo.relation.TeacherQueryVO;
import com.sun.istack.NotNull;
import com.sun.istack.Nullable;

public interface StudentRelationService {

    /**
     * queryTeacher
     * 根据学生ID返回对应校内导师的信息
     */
    TeacherQueryVO queryTeacher(@NotNull String studentId);

    /**
     * queryComInstructor
     * 根据学生ID返回对应企业导师的信息
     */
    @Nullable
    ComInstructorQueryVO queryComInstructor(@NotNull String studentId);

    /**
     * queryChooseGet
     * 根据学生ID返回对应的指导关系信息
     */
    ChooseQueryVO queryChooseGet(@NotNull String studentId);

    /**
     * queryChoosePost
     * 根据学生ID和校内导师ID添加指导关系，返回添加是否成功
     */
    boolean queryChoosePost(@NotNull String studentId, @NotNull String teacherId);

    /**
     * editComInstructor
     * 根据前端数据中的学生ID和企业导师数据修改对应学生的企业导师信息，返回修改是否成功
     */
    boolean editComInstructor(@NotNull EditComParam paramVO);
}
