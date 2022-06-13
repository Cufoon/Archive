package com.manager.service.login;

import com.manager.constant.StatusConstants;
import com.manager.dao.LoginDAO;
import com.manager.entity.UserInfo;
import com.manager.form.ChangePasswordParam;
import com.manager.form.LoginParam;
import com.manager.util.ServiceResult;
import com.manager.vo.login.ChangePasswordResultVO;
import com.manager.vo.login.LoginResultVO;
import com.manager.vo.login.RoleVO;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    private final LoginDAO passwordDAO;

    @Autowired
    public LoginServiceImpl(LoginDAO passwordDAO) {
        this.passwordDAO = passwordDAO;
    }

    /**
     * loginUser
     * 根据前端数据中的用户名和密码执行登录操作，返回登录是否成功
     */
    @Override
    public ServiceResult<LoginResultVO> loginUser(@NonNull LoginParam LP) {
        String username = LP.getUsername();
        String password = LP.getPassword();
        ServiceResult<LoginResultVO> result = new ServiceResult<>();
        try {
            Optional<UserInfo> userAndPassword = passwordDAO.findById(username);
            if (userAndPassword.isPresent()) {
                UserInfo loginObject = userAndPassword.get();
                String userPassword = loginObject.getPassword();
                Integer userType = loginObject.getType();
                if (userPassword.equals(password)) {
                    if (loginObject.getState() == 0) {
                        result.setCode(StatusConstants.CODE_FORBIDDEN_USER).setMessage("账号已禁用");
                    } else {
                        result.setData(new LoginResultVO(true, userType));
                    }
                } else {
                    log.warn("[LoginService] userID = {} Password Incorrect", username);
                    result.setCode(StatusConstants.CODE_WRONG_USER).setMessage("用户名或密码错误");
                }
            } else {
                log.warn("[LoginService] userID = {} Not Exist", username);
                result.setCode(StatusConstants.CODE_WRONG_USER).setMessage("用户名或密码错误");
            }
        } catch (Exception e) {
            log.error("[System] {}", e.toString());
            result.setCode(StatusConstants.CODE_UNFORESEEN).setMessage("未知错误");
        }
        return result;
    }

    /**
     * changePassword
     * 根据前端数据中的用户名、旧密码和新密码修改用户密码，返回修改是否成功
     */
    @Override
    public ChangePasswordResultVO changePassword(@NonNull ChangePasswordParam CPP, String username) {
        String oldPassword = CPP.getOldPassword();
        String newPassword = CPP.getNewPassword();
        ChangePasswordResultVO resultVO = new ChangePasswordResultVO();

        if (oldPassword == null || newPassword == null) {
            resultVO.setChanged(false);
            return resultVO;
        }
        try {
            Optional<UserInfo> userAndPassword = passwordDAO.findById(username);
            if (userAndPassword.isPresent()) {
                UserInfo loginObject = userAndPassword.get();
                String userPassword = loginObject.getPassword();
                if (userPassword.equals(oldPassword)) {
                    loginObject.setPassword(newPassword);
                    passwordDAO.saveAndFlush(loginObject);
                    resultVO.setChanged(true);
                } else {
                    log.warn("[LoginService] userID = {} oldPassword Incorrect", username);
                    resultVO = null;
                }
            } else {
                log.warn("[LoginService] userID = {} Not Exist", username);
                resultVO = null;
            }
        } catch (Exception e) {
            log.error("[System] {}", e.toString());
            resultVO = null;
        }
        return resultVO;
    }

    /**
     * getUserRole
     * 根据用户名返回用户的账号类型
     */
    @Override
    public RoleVO getUserRole(String username) {
        RoleVO resultVO = new RoleVO();
        try {
            Optional<UserInfo> userAndPassword = passwordDAO.findById(username);
            if (userAndPassword.isPresent()) {
                UserInfo loginObject = userAndPassword.get();
                resultVO.setName(loginObject.getName());
                resultVO.setType(loginObject.getType());
                resultVO.setAvatar(loginObject.getAvatar());
            } else {
                log.warn("[LoginService] userID = {} Not Exist", username);
                resultVO = null;
            }
        } catch (Exception e) {
            log.error("[System] {}", e.toString());
            resultVO = null;
        }
        return resultVO;
    }
}
