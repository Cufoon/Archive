package com.manager.service.score;

import com.manager.form.ScoreQueryParam;
import com.manager.vo.score.ScoreQueryVO;

public interface ScoreQueryService {

    /**
     * scoreQuery
     * 根据前端数据中的学生ID返回对应学生的过程评分信息
     */
    ScoreQueryVO scoreQuery(ScoreQueryParam scoreQueryParam);
}
