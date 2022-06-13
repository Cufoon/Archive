package com.manager.controller.student;

import com.manager.constant.SessionFields;
import com.manager.form.document.EditDocParam;
import com.manager.form.document.EditTableParam;
import com.manager.service.document.EditAllService;
import com.manager.service.document.EditTableService;
import com.manager.service.document.LookAllService;
import com.manager.service.document.QueryAllService;
import com.manager.util.ResultWrapper;
import com.manager.vo.ResultVO;
import com.manager.vo.document.student.DocumentQueryVO;
import com.manager.vo.document.student.EditTableVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/student")
public class DocumentController {

    private final QueryAllService queryAllService;

    private final EditAllService editAllService;

    private final EditTableService editTableService;

    private final LookAllService lookAllService;

    @Autowired
    public DocumentController(QueryAllService queryAllService, EditAllService editAllService, EditTableService editTableService, LookAllService lookAllService) {

        this.queryAllService = queryAllService;

        this.editAllService = editAllService;

        this.editTableService = editTableService;

        this.lookAllService = lookAllService;
    }

    /**
     * queryDocument
     * 学生查询表单填写情况
     */
    @GetMapping("/document/query")
    public ResultVO queryDocument(HttpServletRequest req) {
        String studentID = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        if (studentID == null) {
            log.error("[DocumentQueryController]session查询学号失败");
            return ResultWrapper.error("登录信息获取失败");
        }
        ResultVO res;
        DocumentQueryVO documentQueryVO = queryAllService.queryAll(studentID);
        if (documentQueryVO != null) {
            res = ResultWrapper.success("成功", documentQueryVO);
        } else {
            res = ResultWrapper.error("表格情况查询失败");
        }
        return res;
    }

    /**
     * queryTable
     * 学生获取表单默认信息
     */
    @PostMapping("/document/editDefault")
    public ResultVO queryTable(@Valid @RequestBody EditDocParam editDocParam, HttpServletRequest req) {
        String studentID = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        if (studentID == null) {
            log.error("[DocumentEditDefaultController] session查询学号失败");
            return ResultWrapper.error("登录信息获取失败");
        }
        ResultVO res;
        editDocParam.setId(studentID);
        Object object = editAllService.editAll(editDocParam);
        if (object != null) {
            res = ResultWrapper.success("成功", object);
        } else {
            res = ResultWrapper.error("表格情况查询失败");
        }
        return res;
    }

    /**
     * editTable
     * 学生填写表单
     */
    @PostMapping("/document/edit")
    public ResultVO editTable(@Valid @RequestBody EditTableParam editTableParam, HttpServletRequest req) {
        String studentID = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        if (studentID == null) {
            log.error("[TableEditController] session查询学号失败");
            return ResultWrapper.error("登录信息获取失败");
        }
        ResultVO res;
        editTableParam.setId(studentID);
        EditTableVO editTableVO = editTableService.editTable(editTableParam, 1);
        if (editTableVO != null) {
            res = ResultWrapper.success("成功", editTableVO);
        } else {
            res = ResultWrapper.error("表格情况查询失败");
        }
        return res;
    }

    /**
     * temporarySaveTable
     * 保存学生填写表单
     */
    @PostMapping("/document/temporarySave")
    public ResultVO temporarySaveTable(@Valid @RequestBody EditTableParam editTableParam, HttpServletRequest req) {
        String studentID = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        if (studentID == null) {
            log.error("[TemporarySaveController] session查询学号失败");
            return ResultWrapper.error("登录信息获取失败");
        }
        ResultVO res;
        editTableParam.setId(studentID);
        EditTableVO editTableVO = editTableService.editTable(editTableParam, 0);
        if (editTableVO != null) {
            res = ResultWrapper.success("成功", editTableVO);
        } else {
            res = ResultWrapper.error("表格情况查询失败");
        }
        return res;
    }

    @PostMapping("document/look")
    public ResultVO lookTable(@Valid @RequestBody EditDocParam editDocParam, HttpServletRequest req) {
        String studentID = (String) req.getSession().getAttribute(SessionFields.USERNAME);
        if (studentID == null) {
            log.error("[DocumentLookController] session查询学号失败");
            return ResultWrapper.error("登录信息获取失败");
        }
        editDocParam.setId(studentID);
        ResultVO res;
        Object object = lookAllService.lookAll(studentID, editDocParam.getCategory(), editDocParam.getOrder());
        if (object != null) {
            res = ResultWrapper.success("成功", object);
        } else {
            res = ResultWrapper.error("表格情况查询失败");
        }
        return res;
    }
}
