package com.manager.dao;

import com.manager.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginDAO extends JpaRepository<UserInfo, String> {

}
