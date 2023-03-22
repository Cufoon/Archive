package com.manager.dao;

import com.manager.entity.TableSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableSummaryDAO extends JpaRepository<TableSummary, Integer> {

    /**
     * findByStudentId
     * 根据学号查找一个学生提交的一份实习总结报告
     */
    TableSummary findByStudentId(String studentId);

    /**
     * findByTeacherId
     * 根据工号查找该教师所指导的所有学生提交的实习总结报告
     */
    List<TableSummary> findByTeacherId(String teacherId);

    /**
     * findByStudentIdAndStatus
     * 根据学号和状态查找学生提交的一份汇报表
     */
    TableSummary findByStudentIdAndStatus(String studentId, Integer status);
}
