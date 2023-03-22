package com.manager.service.score;

import com.manager.form.ScoreEnterParam;
import com.manager.vo.score.ScoreEnterVO;

public interface ScoreEnterService {

    /**
     * scoreEnter
     * 根据前端数据中的学生ID和学生小组答辩成绩保存对应学生的成绩信息，返回保存是否成功
     */
    ScoreEnterVO scoreEnter(ScoreEnterParam scoreEnterParam);
}
