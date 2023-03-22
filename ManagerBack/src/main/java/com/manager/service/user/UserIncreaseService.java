package com.manager.service.user;

import com.manager.form.UserIncreaseParam;
import com.manager.vo.user.UserIncreaseVO;

public interface UserIncreaseService {

    /**
     * increaseUser
     * 根据前端数据中的用户信息创建用户，返回创建是否成功
     */
    UserIncreaseVO increaseUser(UserIncreaseParam userIncreaseParam);
}
