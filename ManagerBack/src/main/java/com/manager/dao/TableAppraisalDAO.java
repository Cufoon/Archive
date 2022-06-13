package com.manager.dao;

import com.manager.entity.TableAppraisal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableAppraisalDAO extends JpaRepository<TableAppraisal, Integer> {

    /**
     * findByStudentId
     * 根据学号查找一个学生的鉴定表
     */
    TableAppraisal findByStudentId(String studentId);

    /**
     * findByTeacherId
     * 根据工号查找学生提交给教师的鉴定表
     */
    List<TableAppraisal> findByTeacherId(String teacherId);

    /**
     * findByStudentIdAndStatus
     * 根据学号和状态查找学生提交的一份汇报表
     */
    TableAppraisal findByStudentIdAndStatus(String studentId, Integer status);
}
