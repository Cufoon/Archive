package com.manager.controller.student;

import com.manager.constant.SessionFields;
import com.manager.form.UploadArchiveParam;
import com.manager.service.enterprise.StudentEnterpriseService;
import com.manager.util.ResultWrapper;
import com.manager.vo.ResultVO;
import com.manager.vo.enterprise.UploadedArchiveVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/student")
public class ArchiveController {

    private final StudentEnterpriseService studentEnterpriseService;

    @Autowired
    public ArchiveController(StudentEnterpriseService studentEnterpriseService) {
        this.studentEnterpriseService = studentEnterpriseService;
    }

    /**
     * uploadArchive
     */
    @PostMapping("/archive/upload")
    public ResultVO uploadArchive(@Valid @RequestBody UploadArchiveParam uploadArchiveParam, HttpServletRequest request) {
        String id = (String) request.getSession().getAttribute(SessionFields.USERNAME);
        ResultVO resultVO;
        if (id == null) {
            log.error("[uploadArchiveController] session查询用户id失败");
            resultVO = ResultWrapper.error("登录信息获取失败");
        } else {
            resultVO = ResultWrapper.success("成功", studentEnterpriseService.uploadArchive(id, uploadArchiveParam.getArchive()));
        }
        return resultVO;
    }

    @GetMapping("/archive/query")
    public ResultVO queryArchive(HttpServletRequest request) {
        String id = (String) request.getSession().getAttribute(SessionFields.USERNAME);
        Integer role = (Integer) request.getSession().getAttribute(SessionFields.ROLE);
        if (role != 1) {
            return null;
        }
        UploadedArchiveVO uploadedArchiveVO = studentEnterpriseService.downloadArchive(id);
        if (uploadedArchiveVO.getArchivePng() == null) {
            return ResultWrapper.success("未上传", uploadedArchiveVO);
        }
        return ResultWrapper.success("获取成功", uploadedArchiveVO);
    }
}
