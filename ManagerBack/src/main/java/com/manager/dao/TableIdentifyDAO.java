package com.manager.dao;

import com.manager.entity.TableIdentify;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableIdentifyDAO extends JpaRepository<TableIdentify, Integer> {

    /**
     * findByStudentId
     * 根据学号查找一个学生提交的一张鉴定表
     */
    TableIdentify findByStudentId(String studentId);

    /**
     * findByTeacherId
     * 根据工号查找该教师所指导的所有学生提交的所有鉴定表
     */
    List<TableIdentify> findByTeacherId(String teacherId);

    /**
     * findByStudentIdAndStatus
     * 根据学号和状态查找学生提交的一份汇报表
     */
    TableIdentify findByStudentIdAndStatus(String studentId, Integer status);
}
