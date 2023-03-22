package com.manager.controller.student;

import com.manager.constant.SessionFields;
import com.manager.form.EditStudentParam;
import com.manager.service.base.StudentInfoService;
import com.manager.util.ResultWrapper;
import com.manager.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/student")
public class StudentInfoController {

    private final StudentInfoService studentInfoService;

    @Autowired
    public StudentInfoController(StudentInfoService studentInfoService) {
        this.studentInfoService = studentInfoService;
    }

    /**
     * queryInfo
     * 学生查询自己的个人信息
     */
    @GetMapping("/baseInfo/query")
    public ResultVO queryInfo(HttpServletRequest req) {
        String id = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        if (id == null) {
            log.error("[QueryInfoController] session查询用户id失败");
            return ResultWrapper.error("登录信息获取失败");
        }
        return ResultWrapper.success("成功", studentInfoService.getStudentInfo(id));
    }

    /**
     * editInfo
     * 学生编辑个人信息
     */
    @PostMapping("/baseInfo/edit")
    public ResultVO editInfo(
            @Valid @RequestBody EditStudentParam param,
            HttpServletRequest req
    ) {
        String id = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        ResultVO resultVO;
        if (id == null) {
            log.error("[EditInfoController] session查询用户id失败");
            resultVO = ResultWrapper.error("登录信息获取失败");
        } else {
            param.setId(id);
            resultVO = ResultWrapper.success("成功", studentInfoService.editStudentInfo(param));
        }
        return resultVO;
    }
}
