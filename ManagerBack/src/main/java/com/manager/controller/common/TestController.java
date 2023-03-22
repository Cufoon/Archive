package com.manager.controller.common;

import com.manager.constant.SessionFields;
import com.manager.util.ResultWrapper;
import com.manager.vo.ResultVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 测试时使用
 * 用于生成用户 cookie session
 */
@RestController
public class TestController {

    /**
     * makeSession
     * 直接请求生成 session
     */
    @GetMapping("/makeSession/{userId}")
    public ResultVO makeSession(HttpServletRequest request,
                                @PathVariable("userId") String userId) {
        HttpSession session = request.getSession();
        session.setAttribute(SessionFields.USERNAME, userId);
        session.setMaxInactiveInterval(7200);
        return ResultWrapper.success("session 创建成功，用户 username = " + userId, null);
    }

    /**
     * testSession
     * 检查当前会话的 session是否有效
     */
    @GetMapping("/testSession")
    public ResultVO testSession(HttpServletRequest request) {
        String username = (String) request.getAttribute(SessionFields.USERNAME);
        if (username == null) {
            return ResultWrapper.error("session 错误");
        }
        return ResultWrapper.success("session 正常，用户 username = " + username, null);
    }
}
