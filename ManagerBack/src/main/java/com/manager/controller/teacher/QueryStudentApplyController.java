package com.manager.controller.teacher;

import com.manager.constant.SessionFields;
import com.manager.service.relation.ShowStudentApplyService;
import com.manager.util.ResultWrapper;
import com.manager.vo.ResultVO;
import com.manager.vo.relation.ShowStudentApplyVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/teacher")
public class QueryStudentApplyController {

    private final ShowStudentApplyService showStudentApplyService;

    @Autowired
    public QueryStudentApplyController(ShowStudentApplyService showStudentApplyService) {
        this.showStudentApplyService = showStudentApplyService;
    }

    /**
     * queryStudentApply
     * 校内导师查看学生申请列表
     */
    @GetMapping("/apply/query")
    public ResultVO queryStudentApply(HttpServletRequest req) {
        String teacherId = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        if (teacherId == null) {
            log.error("[QueryStudentApplyController] session查询用户id失败");
            return ResultWrapper.error("登录信息获取失败");
        }
        return doStudentApplyQuery(teacherId);
    }

    /**
     * doStudentApplyQuery
     * queryStudentApply 的逻辑处理函数
     */
    private ResultVO doStudentApplyQuery(String teacherId) {
        ShowStudentApplyVO showStudentApplyVO = showStudentApplyService.showStudentApply(teacherId);
        return ResultWrapper.success("成功", showStudentApplyVO);
    }
}
