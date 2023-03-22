package com.manager.controller.student;

import com.manager.constant.SessionFields;
import com.manager.form.AddChooseParam;
import com.manager.form.EditComParam;
import com.manager.service.relation.StudentRelationService;
import com.manager.util.ResultWrapper;
import com.manager.vo.ResultVO;
import com.manager.vo.enterprise.ComInstructorEditVO;
import com.manager.vo.enterprise.ComInstructorQueryVO;
import com.manager.vo.relation.ChooseAddVO;
import com.manager.vo.relation.ChooseQueryVO;
import com.manager.vo.relation.TeacherQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequestMapping("/student")
public class InstructorController {

    private final StudentRelationService studentRelationService;

    @Autowired
    public InstructorController(StudentRelationService studentRelationService) {
        this.studentRelationService = studentRelationService;
    }

    /**
     * queryTeacher
     * 学生查询自己的校内导师
     */
    @GetMapping("/instructor/query")
    public ResultVO queryTeacher(HttpServletRequest req) {
        String studentId = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        if (studentId == null) {
            log.error("[QueryTeacherController] session查询学生学号失败");
            return ResultWrapper.error("登录信息获取失败");
        }
        return doTeacherQuery(studentId);
    }

    /**
     * queryComInstructor
     * 学生查询自己的企业导师
     */
    @GetMapping("/comInstructor/query")
    public ResultVO queryComInstructor(HttpServletRequest req) {
        String studentId = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        if (studentId == null) {
            log.error("[QueryComInstructorController] session查询学号失败");
            return ResultWrapper.error("登录信息获取失败");
        }
        return doComInstructorQuery(studentId);
    }

    /**
     * editComInstructor
     * 学生填写或者修改企业导师的信息
     */
    @PostMapping("/comInstructor/edit")
    public ResultVO editComInstructor(
            @Valid @RequestBody EditComParam param,
            HttpServletRequest req
    ) {
        String studentId = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        if (studentId == null) {
            log.error("[EditComInstructorController] session查询学生学号失败");
            return ResultWrapper.error("登录信息获取失败");
        }
        return doComInstructorEdit(param, studentId);
    }

    /**
     * queryChooseGet
     * 获取可以选择的校内导师列表
     */
    @GetMapping("/instructor/choose")
    public ResultVO queryChooseGet(HttpServletRequest req) {
        String studentId = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        if (studentId == null) {
            log.error("[QueryComInstructorController] session查询学号失败");
            return ResultWrapper.error("登录信息获取失败");
        }
        return doChooseGetQuery(studentId);
    }

    /**
     * queryChoosePost
     * 学生发起选择校内导师申请
     */
    @PostMapping("/instructor/choose")
    public ResultVO queryChoosePost(HttpServletRequest req, @Valid @RequestBody AddChooseParam param) {
        String studentId = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        if (studentId == null) {
            log.error("[QueryComInstructorController] session查询学号失败");
            return ResultWrapper.error("登录信息获取失败");
        }
        return doChoosePostQuery(studentId, param.getTid());
    }

    /**
     * doTeacherQuery
     * queryTeacher 的逻辑处理函数
     */
    private ResultVO doTeacherQuery(@NotNull String studentId) {
        TeacherQueryVO queryVO = studentRelationService.queryTeacher(studentId);
        return ResultWrapper.success("处理成功", queryVO);
    }

    /**
     * doComInstructorQuery
     * queryComInstructor 的逻辑处理函数
     */
    private ResultVO doComInstructorQuery(@NotNull String studentId) {
        ResultVO res;
        ComInstructorQueryVO queryVO = studentRelationService.queryComInstructor(studentId);
        if (queryVO != null) {
            res = ResultWrapper.success("成功", queryVO);
        } else {
            res = ResultWrapper.success("没有填写过企业导师数据", new ComInstructorQueryVO());
        }
        return res;
    }

    /**
     * doComInstructorEdit
     * editComInstructor 的逻辑处理函数
     */
    private ResultVO doComInstructorEdit(EditComParam param, String id) {
        ResultVO resultVO;
        param.setId(id);
        if (studentRelationService.editComInstructor(param)) {
            resultVO = ResultWrapper.success("成功", new ComInstructorEditVO(true));
        } else {
            resultVO = ResultWrapper.success("成功", new ComInstructorEditVO(false));
        }
        return resultVO;
    }

    /**
     * doChooseGetQuery
     * queryChooseGet 的逻辑处理函数
     */
    private ResultVO doChooseGetQuery(@NotNull String studentId) {
        ResultVO res;
        ChooseQueryVO queryVO = studentRelationService.queryChooseGet(studentId);
        if (queryVO != null) {
            res = ResultWrapper.success("成功", queryVO);
        } else {
            res = ResultWrapper.error("指导关系查询失败");
        }
        return res;
    }

    /**
     * doChoosePostQuery
     * queryChoosePost 的逻辑处理函数
     */
    private ResultVO doChoosePostQuery(@NotNull String studentId, @NotNull String teacherId) {
        ResultVO res;
        if (studentRelationService.queryChoosePost(studentId, teacherId)) {
            res = ResultWrapper.success("成功", new ChooseAddVO(true));
        } else {
            res = ResultWrapper.success("成功", new ChooseAddVO(false));
        }
        return res;
    }
}
