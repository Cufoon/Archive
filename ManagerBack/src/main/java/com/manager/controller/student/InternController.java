package com.manager.controller.student;

import com.manager.constant.SessionFields;
import com.manager.form.CreateInternshipParam;
import com.manager.service.enterprise.StudentEnterpriseService;
import com.manager.service.intern.InternService;
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
public class InternController {

    private final StudentEnterpriseService studentEnterpriseService;

    private final InternService internService;

    @Autowired
    public InternController(StudentEnterpriseService studentEnterpriseService, InternService internService) {

        this.studentEnterpriseService = studentEnterpriseService;

        this.internService = internService;
    }

    /**
     * queryInternship
     * 学生查询自己的实习信息
     */
    @GetMapping("/internInfo/query")
    public ResultVO queryInternship(HttpServletRequest request) {
        String username = (String) request.getSession().getAttribute(SessionFields.USERNAME);
        return ResultWrapper.success("success", studentEnterpriseService.queryInternship(username));
    }

    /**
     * createInternship
     * 学生新建实习信息
     */
    @PostMapping("/internInfo/edit")
    public ResultVO createInternship(
            @Valid @RequestBody CreateInternshipParam param,
            HttpServletRequest req
    ) {
        String id = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        param.setId(id);
        if (id == null) {
            log.error("[InternController] session查询用户id失败");
            return ResultWrapper.error("登录信息获取失败");
        }
        return doInternshipCreate(param);
    }

    /**
     * isPeriod
     * 学生是否在实习期内
     */
    @GetMapping("/internInfo/isPeriod")
    public ResultVO isPeriod(HttpServletRequest req) {
        String id = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        if (id == null) {
            log.error("[InternController] session查询用户id失败");
            return ResultWrapper.error("登录信息获取失败");
        }
        return ResultWrapper.success("成功", internService.isPeriod(id));
    }

    /**
     * doInternshipCreate
     * createInternship 的逻辑处理函数
     */
    private ResultVO doInternshipCreate(@Valid @RequestBody CreateInternshipParam param) {
        return ResultWrapper.success("成功", studentEnterpriseService.createInternship(param));
    }
}
