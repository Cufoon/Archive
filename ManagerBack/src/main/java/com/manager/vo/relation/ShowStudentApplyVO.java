package com.manager.vo.relation;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ShowStudentApplyVO {

    List<StudentApplyInfoVO> studentApplyList = new ArrayList<>();
}
