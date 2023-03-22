package com.manager.controller.admin;

import com.manager.service.count.CountPeopleService;
import com.manager.util.ResultWrapper;
import com.manager.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/admin")
public class CountManageController {

    private final CountPeopleService countPeopleService;

    public CountManageController(CountPeopleService countPeopleService) {

        this.countPeopleService = countPeopleService;
    }

    @GetMapping("count")
    public ResultVO conutPeople(HttpServletRequest request) {
        ResultVO res;
        return ResultWrapper.success("成功", countPeopleService.countPeople());
    }
}
