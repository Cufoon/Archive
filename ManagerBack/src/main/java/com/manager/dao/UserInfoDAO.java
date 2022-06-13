package com.manager.dao;

import com.manager.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInfoDAO extends JpaRepository<UserInfo, String> {

    /**
     * findAllByType
     * 根据用户类型查找用户列表，type：1学生，2教师，3管理员
     */
    List<UserInfo> findAllByType(Integer type);

    /**
     * findById
     * 根据id查找用户
     */
    UserInfo findAllById(String id);
}
