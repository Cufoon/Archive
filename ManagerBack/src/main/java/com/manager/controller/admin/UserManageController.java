package com.manager.controller.admin;

import com.manager.constant.SessionFields;
import com.manager.form.UserBanOrStartParam;
import com.manager.form.UserIncreaseParam;
import com.manager.service.user.UserBanOrStartService;
import com.manager.service.user.UserIncreaseService;
import com.manager.service.user.UserQueryService;
import com.manager.util.ResultWrapper;
import com.manager.vo.ResultVO;
import com.sun.istack.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/admin")
public class UserManageController {

    private final UserIncreaseService userIncreaseService;

    private final UserQueryService userQueryService;

    private final UserBanOrStartService userBanOrStartService;

    @Autowired
    public UserManageController(UserIncreaseService userIncreaseService, UserQueryService userQueryService, UserBanOrStartService userBanOrStartService) {

        this.userIncreaseService = userIncreaseService;

        this.userQueryService = userQueryService;

        this.userBanOrStartService = userBanOrStartService;
    }

    /**
     * queryUser
     * 按用户类型查询用户列表
     */
    @GetMapping("/user/query")
    public ResultVO queryUser(@NotNull @RequestParam(value = "type") Integer type, HttpServletRequest req) {
        String id = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        if (id == null) {
            log.error("[QueryUserController] session查询用户id失败");
            return ResultWrapper.error("登录信息获取失败");
        }
        return ResultWrapper.success("成功", userQueryService.queryUser(type));
    }

    /**
     * increaseUser
     * 创建新的用户
     */
    @PostMapping("/user/increase")
    public ResultVO increaseUser(
            @Valid @RequestBody UserIncreaseParam param,
            HttpServletRequest req
    ) {
        String id = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        ResultVO resultVO;
        if (id == null) {
            log.error("[IncreaseUserController] session查询用户id失败");
            resultVO = ResultWrapper.error("登录信息获取失败");
        } else {
            param.setId(id);
            resultVO = ResultWrapper.success("成功", userIncreaseService.increaseUser(param));
        }
        return resultVO;
    }

    /**
     * banUser
     * 禁用用户
     */
    @PostMapping("/user/ban")
    public ResultVO banUser(@Valid @RequestBody UserBanOrStartParam userBanOrStartParam, HttpServletRequest req) {
        String id = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        ResultVO resultVO;
        if (id == null) {
            log.error("[BanUserController] session查询用户id失败");
            resultVO = ResultWrapper.error("登录信息获取失败");
        } else {
            resultVO = ResultWrapper.success("成功", userBanOrStartService.banUser(userBanOrStartParam.getId(), 0));
        }
        return resultVO;
    }

    /**
     * startUser
     * 启用用户
     */
    @PostMapping("/user/start")
    public ResultVO startUser(@Valid @RequestBody UserBanOrStartParam userBanOrStartParam, HttpServletRequest req) {
        String id = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        ResultVO resultVO;
        if (id == null) {
            log.error("[StartUserController] session查询用户id失败");
            resultVO = ResultWrapper.error("登录信息获取失败");
        } else {
            resultVO = ResultWrapper.success("成功", userBanOrStartService.banUser(userBanOrStartParam.getId(), 1));
        }
        return resultVO;
    }
}
