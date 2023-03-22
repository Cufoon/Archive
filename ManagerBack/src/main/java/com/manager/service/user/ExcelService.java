package com.manager.service.user;

import com.manager.vo.user.ExcelUploadVO;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;

@Service
public interface ExcelService {

    ExcelUploadVO saveExcel(InputStream inputStream);

    OutputStream giveExcel(OutputStream outputStream, String studengID);
}
