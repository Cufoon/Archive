package com.manager.service.login;

import com.manager.form.ChangePasswordParam;
import com.manager.form.LoginParam;
import com.manager.util.ServiceResult;
import com.manager.vo.login.ChangePasswordResultVO;
import com.manager.vo.login.LoginResultVO;
import com.manager.vo.login.RoleVO;
import lombok.NonNull;

public interface LoginService {

    /**
     * loginUser
     * 根据前端数据中的用户名和密码执行登录操作，返回登录是否成功
     */
    ServiceResult<LoginResultVO> loginUser(@NonNull LoginParam LP);

    /**
     * changePassword
     * 根据前端数据中的用户名、旧密码和新密码修改用户密码，返回修改是否成功
     */
    ChangePasswordResultVO changePassword(@NonNull ChangePasswordParam CPP, String username);

    /**
     * getUserRole
     * 根据用户名返回用户的账号类型
     */
    RoleVO getUserRole(String username);
}
