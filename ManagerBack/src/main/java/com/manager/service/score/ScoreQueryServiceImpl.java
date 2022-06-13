package com.manager.service.score;

import com.manager.dao.StudentScoreDAO;
import com.manager.dao.UserInfoDAO;
import com.manager.entity.StudentScore;
import com.manager.entity.UserInfo;
import com.manager.form.ScoreQueryParam;
import com.manager.vo.score.ScoreQueryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScoreQueryServiceImpl implements ScoreQueryService {

    private final StudentScoreDAO studentScoreDAO;

    private final UserInfoDAO userInfoDAO;

    @Autowired
    public ScoreQueryServiceImpl(StudentScoreDAO studentScoreDAO, UserInfoDAO userInfoDAO) {

        this.studentScoreDAO = studentScoreDAO;

        this.userInfoDAO = userInfoDAO;
    }

    /**
     * scoreQuery
     * 根据前端数据中的学生ID返回对应学生的过程评分信息
     */
    @Override
    public ScoreQueryVO scoreQuery(ScoreQueryParam scoreQueryParam) {
        ScoreQueryVO scoreQueryVO = new ScoreQueryVO();
        StudentScore studentScore = studentScoreDAO.findByStudentId(scoreQueryParam.getStudentId());
        UserInfo userInfo = userInfoDAO.findById(scoreQueryParam.getStudentId()).get();
        if (studentScore == null) {
            studentScore = new StudentScore();
        }
        scoreQueryVO.setStudentId(scoreQueryParam.getStudentId());
        scoreQueryVO.setName(userInfo.getName());
        Integer score = studentScore.getReportScore1();
        scoreQueryVO.setReportScore1(score);
        score = studentScore.getReportScore2();
        scoreQueryVO.setReportScore2(score);
        score = studentScore.getReportScore3();
        scoreQueryVO.setReportScore3(score);
        score = studentScore.getExamScore1();
        scoreQueryVO.setExamScore1(score);
        score = studentScore.getExamScore2();
        scoreQueryVO.setExamScore2(score);
        score = studentScore.getExamScore3();
        scoreQueryVO.setExamScore3(score);
        score = studentScore.getIdentifyScore();
        scoreQueryVO.setIdentifyScore(score);
        score = studentScore.getAppraisalScore();
        scoreQueryVO.setAppraisalScore(score);
        score = studentScore.getSummaryScore();
        scoreQueryVO.setSummaryScore(score);
        scoreQueryVO.setGroupScore(studentScore.getGroupScore());
        scoreQueryVO.setSumScore(studentScore.getSumScore());

        return scoreQueryVO;
    }
}
