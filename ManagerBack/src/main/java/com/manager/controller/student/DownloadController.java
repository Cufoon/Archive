package com.manager.controller.student;

import com.manager.constant.SessionFields;
import com.manager.service.user.ExcelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

@Slf4j
@RestController
@RequestMapping("/student")
public class DownloadController {

    private final ExcelService excelService;

    @Autowired
    public DownloadController(ExcelService excelService) {
        this.excelService = excelService;
    }

    /**
     * 下载文件
     */
    @GetMapping("/api/excelDownload")
    public void download(HttpServletRequest request, HttpServletResponse response) {
        String studentID = (String) request.getSession().getAttribute(SessionFields.USERNAME);
        if (studentID == null) {
            log.error("[DocumentQueryController]session查询学号失败");
        }
        try {
            //设置返回值信息
//            String fileName = "appraisal.xlsx";
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + studentID + "_table_identify.xlsx");
            OutputStream outputStream = response.getOutputStream();
            //在这一步之前可以对workbook赋值，它只是一个excel文件对象，可以用代码赋值写入你想要的excel数据
            excelService.giveExcel(outputStream, studentID);
        } catch (Exception e) {
//            logger.error("----exportFile error----",e);
        }
//        return ResultWrapper.success("成功", );
    }
}
