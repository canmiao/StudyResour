package com.mgjt.community.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Github初次回调
 */
@Controller
public class AuthorizeController {
    public String callback(@RequestParam(name = "code")String code,
                           @RequestParam(name = "state")String state) {
        return "index";
    }

}
