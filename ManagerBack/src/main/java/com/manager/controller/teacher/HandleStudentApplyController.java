package com.manager.controller.teacher;

import com.manager.constant.SessionFields;
import com.manager.form.HandleStudentApplyParam;
import com.manager.service.relation.HandleStudentApplyService;
import com.manager.util.ResultWrapper;
import com.manager.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/teacher")
public class HandleStudentApplyController {

    private final HandleStudentApplyService handleStudentApplyService;

    @Autowired
    public HandleStudentApplyController(HandleStudentApplyService handleStudentApplyService) {
        this.handleStudentApplyService = handleStudentApplyService;
    }

    /**
     * acceptStudentApply
     * 校内导师接受学生的申请
     */
    @PostMapping("apply/accept")
    public ResultVO acceptStudentApply(
            HttpServletRequest req,
            @Valid @RequestBody HandleStudentApplyParam param
    ) {
        String teacherId = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        param.setId(teacherId);
        if (teacherId == null) {
            log.error("[AcceptStudentApplyController] session查询用户id失败");
            return ResultWrapper.error("登录信息获取失败");
        }
        return doStudentApplyAccept(param);
    }

    /**
     * doStudentApplyAccept
     * acceptStudentApply 的逻辑处理函数
     */
    private ResultVO doStudentApplyAccept(@Valid @RequestBody HandleStudentApplyParam param) {
        return ResultWrapper.success("成功", handleStudentApplyService.handleStudentApply(param, 1));
    }

    /**
     * rejectStudentApply
     * 校内导师拒绝学生的申请
     */
    @PostMapping("apply/reject")
    public ResultVO rejectStudentApply(
            HttpServletRequest req,
            @Valid @RequestBody HandleStudentApplyParam param
    ) {
        String teacherId = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        param.setId(teacherId);
        if (teacherId == null) {
            log.error("[AcceptStudentApplyController] session查询用户id失败");
            return ResultWrapper.error("登录信息获取失败");
        }
        return doStudentApplyReject(param);
    }

    /**
     * doStudentApplyReject
     * rejectStudentApply 的逻辑处理函数
     */
    private ResultVO doStudentApplyReject(@Valid @RequestBody HandleStudentApplyParam param) {
        return ResultWrapper.success("成功", handleStudentApplyService.handleStudentApply(param, 2));
    }
}
