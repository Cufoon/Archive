package com.manager.vo.query;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class QueryAllStudentVO {

    List<MyStudentVO> studentList;
}
