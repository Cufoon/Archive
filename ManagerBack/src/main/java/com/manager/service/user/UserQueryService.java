package com.manager.service.user;

import com.manager.vo.user.UserQueryVO;

public interface UserQueryService {

    /**
     * queryUser
     * 根据用户类型返回用户列表
     */
    UserQueryVO queryUser(Integer type);
}
