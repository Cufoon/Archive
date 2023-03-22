package com.manager.controller.common;

import com.manager.constant.SessionFields;
import com.manager.form.ChangePasswordParam;
import com.manager.form.LoginParam;
import com.manager.service.login.LoginService;
import com.manager.util.ResultWrapper;
import com.manager.util.ServiceResult;
import com.manager.vo.ResultVO;
import com.manager.vo.login.ChangePasswordResultVO;
import com.manager.vo.login.LoginResultVO;
import com.manager.vo.login.LogoutResultVO;
import com.manager.vo.login.RoleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@RestController
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * login
     * 用户登录
     */
    @PostMapping("/login")
    public ResultVO login(
            @Valid @RequestBody LoginParam param,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        ResultVO resultVO;
        ServiceResult<LoginResultVO> loginResult = loginService.loginUser(param);
        if (loginResult.isSuccess()) {
            String username = param.getUsername();
            HttpSession session = request.getSession();
            session.setAttribute(SessionFields.USERNAME, username);
            session.setAttribute(SessionFields.ROLE, loginResult.getData().getType());
            session.setMaxInactiveInterval(7200);
            response.setHeader("Set-Cookie", "JSESSIONID=" + session.getId() + "; path=/; Secure; SameSite=None; HttpOnly");
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS");
            response.setHeader("Access-Control-Max-Age", "86400");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type");
            response.setHeader("Access-Control-Expose-Headers", "*");
            resultVO = ResultWrapper.success("Login Success", loginResult.getData());
        } else {
            resultVO = ResultWrapper.error(loginResult.getCode(), loginResult.getMessage());
        }
        return resultVO;
    }

    /**
     * changePassword
     * 用户修改密码
     */
    @PostMapping("/changePassword")
    public ResultVO changePassword(
            @Valid @RequestBody ChangePasswordParam param,
            HttpServletRequest request
    ) {
        String username = (String) request.getSession().getAttribute(SessionFields.USERNAME);
        ResultVO resultVO;
        ChangePasswordResultVO changePasswordResultVO = loginService.changePassword(param, username);
        if (changePasswordResultVO != null) {
            resultVO = ResultWrapper.success("Change Password Success", changePasswordResultVO);
        } else {
            // todo: 结果封装代码需要重构
            resultVO = ResultWrapper.error("原密码错误");
        }
        return resultVO;
    }

    /**
     * getUserRole
     * 获取用户角色类型
     */
    @GetMapping("/role")
    public ResultVO getUserRole(HttpServletRequest request, HttpServletResponse response) {
        String username = (String) request.getSession().getAttribute(SessionFields.USERNAME);
        ResultVO resultVO;
        RoleVO roleVO = loginService.getUserRole(username);
        if (roleVO != null) {
            resultVO = ResultWrapper.success("Get UserRole Success", roleVO);
        } else {
            resultVO = ResultWrapper.error("error");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
        return resultVO;
    }

    /**
     * logout
     * 退出登录
     */
    @GetMapping("/logout")
    public ResultVO logout(HttpServletRequest request) {
        request.getSession().removeAttribute(SessionFields.USERNAME);
        request.getSession().removeAttribute(SessionFields.ROLE);
        LogoutResultVO logoutResultVO = new LogoutResultVO();
        logoutResultVO.setLogout(true);
        return ResultWrapper.success("Logout Success", logoutResultVO);
    }
}
