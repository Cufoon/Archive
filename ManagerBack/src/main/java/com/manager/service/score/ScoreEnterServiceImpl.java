package com.manager.service.score;

import com.manager.dao.StudentScoreDAO;
import com.manager.entity.StudentScore;
import com.manager.form.ScoreEnterParam;
import com.manager.vo.score.ScoreEnterVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScoreEnterServiceImpl implements ScoreEnterService {

    private final StudentScoreDAO studentScoreDAO;

    @Autowired
    public ScoreEnterServiceImpl(StudentScoreDAO studentScoreDAO) {
        this.studentScoreDAO = studentScoreDAO;
    }

    /**
     * scoreEnter
     * 根据前端数据中的学生ID和学生小组答辩成绩保存对应学生的成绩信息，返回保存是否成功
     */
    @Override
    public ScoreEnterVO scoreEnter(ScoreEnterParam scoreEnterParam) {
        ScoreEnterVO scoreEnterVO = new ScoreEnterVO();
        StudentScore studentScore = studentScoreDAO.findByStudentId(scoreEnterParam.getStudentId());
        // 根据学生分数情况选择更新或是新建
        if (studentScore == null) {
            studentScore = new StudentScore();
            studentScore.setStudentId(scoreEnterParam.getStudentId());
        }
        studentScore.setGroupScore(scoreEnterParam.getGroupScore());
        // 实时更新学生总分
        double sumScore = (double)
                (studentScore.getReportScore1() == null ? 0 : studentScore.getReportScore1())
                + (studentScore.getReportScore2() == null ? 0 : studentScore.getReportScore2())
                + (studentScore.getReportScore3() == null ? 0 : studentScore.getReportScore3())
                + (studentScore.getExamScore1() == null ? 0 : studentScore.getExamScore1())
                + (studentScore.getExamScore2() == null ? 0 : studentScore.getExamScore2())
                + (studentScore.getExamScore3() == null ? 0 : studentScore.getExamScore3())
                + (studentScore.getIdentifyScore() == null ? 0 : studentScore.getIdentifyScore())
                + (studentScore.getAppraisalScore() == null ? 0 : studentScore.getAppraisalScore())
                + (studentScore.getSummaryScore() == null ? 0 : studentScore.getSummaryScore())
                + studentScore.getGroupScore() * 0.55;
        studentScore.setSumScore(sumScore);
        studentScoreDAO.saveAndFlush(studentScore);
        scoreEnterVO.setEntered(true);
        return scoreEnterVO;
    }
}
