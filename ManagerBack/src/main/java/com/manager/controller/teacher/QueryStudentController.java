package com.manager.controller.teacher;

import com.manager.constant.SessionFields;
import com.manager.form.QueryStudentParam;
import com.manager.service.query.StudentConcreteService;
import com.manager.service.query.StudentGeneralService;
import com.manager.util.ResultWrapper;
import com.manager.vo.ResultVO;
import com.manager.vo.query.QueryStudentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/teacher")
public class QueryStudentController {

    private final StudentConcreteService studentConcreteService;

    private final StudentGeneralService studentGeneralService;

    @Autowired
    public QueryStudentController(StudentConcreteService studentConcreteService,
                                  StudentGeneralService studentGeneralService) {
        this.studentConcreteService = studentConcreteService;
        this.studentGeneralService = studentGeneralService;
    }

    /**
     * queryStudentConcrete
     * 校内导师查看学生详细信息
     */
    @PostMapping("/queryStudent")
    public ResultVO queryStudentConcrete(@Valid @RequestBody QueryStudentParam param) {
        QueryStudentVO concrete = studentConcreteService.queryConcreteStudent(param.getStudentId());
        ResultVO resultVO;
        if (concrete == null) {
            resultVO = ResultWrapper.error("学生详情信息查询失败");
        } else {
            resultVO = ResultWrapper.success("成功", concrete);
        }
        return resultVO;
    }

    /**
     * queryAllStudent
     * 校内导师查询自己所带的所有学生
     */
    @GetMapping("/queryAllStudents")
    public ResultVO queryAllStudent(HttpServletRequest req) {
        String teacherId = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        if (teacherId == null) {
            log.error("[QueryStudentController] 获取登录信息失败");
            return ResultWrapper.error("查询学生信息失败");
        }
        return doAllStudentQuery(teacherId);
    }

    /**
     * doAllStudentQuery
     * queryAllStudent 的逻辑处理函数
     */
    private ResultVO doAllStudentQuery(String teacherId) {
        return ResultWrapper.success("成功", studentGeneralService.queryAllStudent(teacherId, 1));
    }
}
