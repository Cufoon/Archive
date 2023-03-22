package com.manager.controller.admin;

import com.manager.constant.SessionFields;
import com.manager.form.EditInternParam;
import com.manager.service.intern.InternService;
import com.manager.util.ResultWrapper;
import com.manager.vo.ResultVO;
import com.manager.vo.intern.EditInternVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminInternController {

    private final InternService internService;

    @Autowired
    public AdminInternController(InternService internService) {
        this.internService = internService;
    }

    @PostMapping("/editPeriod")
    public ResultVO editIntern(
            @NotNull @RequestBody EditInternParam param,
            HttpServletRequest req
    ) {
        String id = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        if (id == null) {
            log.error("[editIntern] 登录信息失效");
            return ResultWrapper.error("登录信息失效");
        }
        boolean b = internService.editPeriod(param);
        return ResultWrapper.success("编辑成功", new EditInternVO(b));
    }

    @GetMapping("/getPeriod")
    public ResultVO queryPeriodList(HttpServletRequest req) {
        // String id = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        Integer role = (Integer) req.getSession().getAttribute(SessionFields.ROLE);
        if (role != 3) {
            return null;
        }
        return ResultWrapper.success("Get Period List Success", internService.getPeriodList());
    }
}
