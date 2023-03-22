package com.manager.service.enterprise;

import com.manager.form.CreateInternshipParam;
import com.manager.vo.enterprise.InternshipCreateVO;
import com.manager.vo.enterprise.InternshipVO;
import com.manager.vo.enterprise.UploadArchiveVO;
import com.manager.vo.enterprise.UploadedArchiveVO;

public interface StudentEnterpriseService {

    /**
     * createInternship
     * 根据前端数据中的学生ID和实习相关信息保存对应学生的实习信息，返回保存是否成功
     */
    InternshipCreateVO createInternship(CreateInternshipParam createInternshipParam);

    /**
     * queryInternship
     * 根据学生ID返回对应学生的企业实习信息
     */
    InternshipVO queryInternship(String username);

    /**
     * queryInternship
     * 根据学生ID返回对应学生的企业实习信息
     */
    UploadArchiveVO uploadArchive(String studentId, String archive);

    /**
     * downloadArchive
     * 根据学生ID返回对应学生的鉴定表的图片
     */
    UploadedArchiveVO downloadArchive(String studentId);
}
