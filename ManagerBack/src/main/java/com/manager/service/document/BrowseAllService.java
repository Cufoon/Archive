package com.manager.service.document;

import com.manager.vo.document.teacher.DocumentBrowseVO;
import com.sun.istack.NotNull;

public interface BrowseAllService {

    /**
     * browseAll
     * 根据校内导师ID返回所有学生填写过的表单的基础信息
     */
    DocumentBrowseVO browseAll(@NotNull String teacherId);
}
