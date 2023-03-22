package com.manager.controller.teacher;

import com.manager.constant.SessionFields;
import com.manager.form.EditTeacherParam;
import com.manager.service.base.TeacherInfoService;
import com.manager.util.ResultWrapper;
import com.manager.vo.ResultVO;
import com.manager.vo.base.TeacherInfoQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/teacher")
public class TeacherInfoController {

    private final TeacherInfoService teacherInfoService;

    @Autowired
    public TeacherInfoController(TeacherInfoService infoService) {
        this.teacherInfoService = infoService;
    }

    /**
     * queryInfo
     * 校内导师查看个人信息
     */
    @GetMapping("/info/query")
    public ResultVO queryInfo(HttpServletRequest req) {
        String id = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        TeacherInfoQueryVO teacherInfo = teacherInfoService.getTeacherInfo(id);
        ResultVO resultVO;
        if (teacherInfo != null) {
            resultVO = ResultWrapper.success("成功", teacherInfo);
        } else {
            resultVO = ResultWrapper.error("未查询到数据");
        }
        return resultVO;
    }

    /**
     * editInfo
     * 校内导师编辑个人信息
     */
    @PostMapping("info/edit")
    public ResultVO editInfo(
            HttpServletRequest req,
            @Valid @RequestBody EditTeacherParam param
    ) {
        String id = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        param.setId(id);
        ResultVO resultVO;
        if (id == null) {
            log.error("[EditInfoController] session查询用户id失败");
            resultVO = ResultWrapper.error("登录信息获取失败");
        } else {
            resultVO = ResultWrapper.success("成功", teacherInfoService.editTeacherInfo(param));
        }
        return resultVO;
    }
}
