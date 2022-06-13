package com.manager.vo.score;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ScoreQueryVO {

    @JsonProperty("sid")
    private String studentId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("score1")
    private Integer reportScore1;

    @JsonProperty("score2")
    private Integer reportScore2;

    @JsonProperty("score3")
    private Integer reportScore3;

    @JsonProperty("score4")
    private Integer examScore1;

    @JsonProperty("score5")
    private Integer examScore2;

    @JsonProperty("score6")
    private Integer examScore3;

    @JsonProperty("score7")
    private Integer identifyScore;

    @JsonProperty("score8")
    private Integer appraisalScore;

    @JsonProperty("score9")
    private Integer summaryScore;

    @JsonProperty("score10")
    private Double groupScore;

    @JsonProperty("sum")
    private Double sumScore;
}
