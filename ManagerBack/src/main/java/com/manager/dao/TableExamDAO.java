package com.manager.dao;

import com.manager.entity.TableExam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableExamDAO extends JpaRepository<TableExam, Integer> {

    /**
     * findByStudentId
     * 根据学号查找一个学生提交的所有检查表
     */
    List<TableExam> findByStudentId(String studentId);

    /**
     * findByTeacherId
     * 根据工号查找学生提交给教师的所有检查表
     */
    List<TableExam> findByTeacherId(String teacherId);

    /**
     * findByStudentIdAndOrderNum
     * 根据学号和提交号查找学生提交的一张检查表
     */
    TableExam findByStudentIdAndOrderNum(String studentId, Integer order);

    /**
     * findByStudentIdAndOrderNumAndStatus
     * 根据学号和提交号和状态查找学生提交的一份汇报表
     */
    TableExam findByStudentIdAndOrderNumAndStatus(String studentId, Integer orderNum, Integer status);
}
