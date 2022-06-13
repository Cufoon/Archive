package com.manager.controller.teacher;

import com.manager.constant.SessionFields;
import com.manager.form.document.HandleTableParam;
import com.manager.form.document.LookParam;
import com.manager.service.document.BrowseAllService;
import com.manager.service.document.HandleAllService;
import com.manager.service.document.HandleTableService;
import com.manager.service.document.LookAllService;
import com.manager.util.ResultWrapper;
import com.manager.vo.ResultVO;
import com.manager.vo.document.teacher.DocumentBrowseVO;
import com.manager.vo.document.teacher.HandleTableVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/teacher")
public class DocumentHandleController {

    private final BrowseAllService browseAllService;

    private final HandleAllService handleAllService;

    private final HandleTableService handleTableService;

    private final LookAllService lookAllService;

    public DocumentHandleController(BrowseAllService browseAllService, HandleAllService handleAllService, HandleTableService handleTableService, LookAllService lookAllService) {

        this.browseAllService = browseAllService;

        this.handleAllService = handleAllService;

        this.handleTableService = handleTableService;

        this.lookAllService = lookAllService;
    }

    /**
     * browseAll
     * 校内导师查看表单提交情况
     */
    @GetMapping("/document/browse")
    public ResultVO browseAll(HttpServletRequest req) {
        String teacherID = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        if (teacherID == null) {
            log.error("[DocumentBrowseController]session查询工号失败");
            return ResultWrapper.error("登录信息获取失败");
        }
        ResultVO res;
        DocumentBrowseVO documentBrowseVO = browseAllService.browseAll(teacherID);
        if (documentBrowseVO != null) {
            res = ResultWrapper.success("成功", documentBrowseVO);
        } else {
            res = ResultWrapper.error("表格情况查询失败");
        }
        return res;
    }

    /**
     * handleAll
     * 校内导师获取学生提交的表单
     */
    @GetMapping("/document/handle")
    public ResultVO handleAll(@RequestParam(value = "sid") String sid,
                              @RequestParam(value = "category") Integer category,
                              @RequestParam(value = "order") Integer order,
                              HttpServletRequest req) {
        String teacherID = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        if (teacherID == null) {
            log.error("[DocumentHandleController] session查询工号失败");
            return ResultWrapper.error("登录信息获取失败");
        }
        ResultVO res;
        Object object = handleAllService.handleAll(teacherID, sid, category, order);
        if (object != null) {
            res = ResultWrapper.success("成功", object);
        } else {
            res = ResultWrapper.error("表格情况查询失败");
        }
        return res;
    }

    /**
     * handleTable
     * 校内导师审核表单
     */
    @PostMapping("/document/handle")
    public ResultVO handleTable(@Valid @RequestBody HandleTableParam handleTableParam, HttpServletRequest req) {
        String teacherID = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        if (teacherID == null) {
            log.error("[TableHandleController] session查询工号失败");
            return ResultWrapper.error("登录信息获取失败");
        }
        ResultVO res;
        HandleTableVO handleTableVO = handleTableService.handleTable(handleTableParam, 3);
        if (handleTableVO != null) {
            res = ResultWrapper.success("成功", handleTableVO);
        } else {
            res = ResultWrapper.error("表格情况查询失败");
        }
        return res;
    }

    /**
     * rejectTable
     * 校内导师打回表单
     */
    @PostMapping("/document/reject")
    public ResultVO rejectTable(@Valid @RequestBody HandleTableParam handleTableParam, HttpServletRequest req) {
        String teacherID = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        if (teacherID == null) {
            log.error("[TableRejectController] session查询工号失败");
            return ResultWrapper.error("登录信息获取失败");
        }
        ResultVO res;
        HandleTableVO handleTableVO = handleTableService.handleTable(handleTableParam, 2);
        if (handleTableVO != null) {
            res = ResultWrapper.success("成功", handleTableVO);
        } else {
            res = ResultWrapper.error("表格情况查询失败");
        }
        return res;
    }

    @PostMapping("document/look")
    public ResultVO lookTable(@Valid @RequestBody LookParam lookParam, HttpServletRequest req) {
        String teacherID = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        if (teacherID == null) {
            log.error("[DocumentLookController] session查询工号失败");
            return ResultWrapper.error("登录信息获取失败");
        }
        ResultVO res;
        Object object = lookAllService.lookAll(lookParam.getId(), lookParam.getCategory(), lookParam.getOrder());
        if (object != null) {
            res = ResultWrapper.success("成功", object);
        } else {
            res = ResultWrapper.error("表格情况查询失败");
        }
        return res;
    }
}
