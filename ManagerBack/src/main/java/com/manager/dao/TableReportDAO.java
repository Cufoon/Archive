package com.manager.dao;

import com.manager.entity.TableReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableReportDAO extends JpaRepository<TableReport, Integer> {

    /**
     * findByStudentId
     * 根据学号查找一个学生提交的所有汇报表
     */
    List<TableReport> findByStudentId(String studentId);

    /**
     * findByTeacherId
     * 根据工号查找该教师所指导的所有学生提交的汇报表
     */
    List<TableReport> findByTeacherId(String teacherId);

    /**
     * findByStudentIdAndOrderNum
     * 根据学号和提交号查找学生提交的一份汇报表
     */
    TableReport findByStudentIdAndOrderNum(String studentId, Integer orderNum);

    /**
     * findByStudentIdAndOrderNumAndStatus
     * 根据学号和提交号和状态查找学生提交的一份汇报表
     */
    TableReport findByStudentIdAndOrderNumAndStatus(String studentId, Integer orderNum, Integer status);
}
