package com.heifan.code.test.controller;

import cn.hutool.core.lang.Validator;
import com.heifan.code.domain.result.Result;
import com.heifan.code.exception.BizException;
import com.heifan.code.exception.MessageCodeEnum;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping(Test01Controller.ROUTER)
@RestController
public class Test01Controller {

    static final String ROUTER = "/test/01";

    @PostMapping("/get")
    public Object getTest(HttpServletRequest request, HttpServletResponse response){
        return Result.success("post success!");
    }
}
