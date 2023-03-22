package com.manager.controller.admin;

import com.manager.service.user.ExcelService;
import com.manager.util.ResultWrapper;
import com.manager.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

@Slf4j
@RestController
@RequestMapping("/admin")
public class ExcelController {

    private final ExcelService excelService;

    public ExcelController(ExcelService excelService) {
        this.excelService = excelService;
    }

    /**
     * 用于接收前端上传文件
     *
     * @param request
     * @param mould
     */
    @PostMapping("/excelUpload")
    public ResultVO excelUpload(HttpServletRequest request, @RequestParam("file") MultipartFile mould) throws Exception {
        //file对象名和前端name属性值一致
        InputStream inputStream = null;
        try {
            inputStream = mould.getInputStream();
        } catch (Exception e) {
        }
        return ResultWrapper.success("成功", excelService.saveExcel(inputStream));
    }
}
