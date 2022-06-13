package com.manager.vo.relation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.manager.entity.StudentTeacherRelation;
import com.manager.entity.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RelationListVO {

    public RelationListVO(UserInfo u, UserInfo uu, StudentTeacherRelation str) {
        this.tid = u.getId();
        this.tname = u.getName();
        this.sid = uu.getId();
        this.sname = uu.getName();
        this.sendTime = str.getSendTime().toString();
        this.dealTime = str.getDealTime().toString();
    }

    @JsonProperty("tid")
    private String tid;

    @JsonProperty("tname")
    private String tname;

    @JsonProperty("sid")
    private String sid;

    @JsonProperty("sname")
    private String sname;

    @JsonProperty("sendTime")
    private String sendTime;

    @JsonProperty("dealTime")
    private String dealTime;
}
