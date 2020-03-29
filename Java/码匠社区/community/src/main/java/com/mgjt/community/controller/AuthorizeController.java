package com.mgjt.community.controller;

import com.mgjt.community.bean.UserInfo;
import com.mgjt.community.dto.AccessTokenDTO;
import com.mgjt.community.dto.GithubUser;
import com.mgjt.community.mapper.UserInfoMapper;
import com.mgjt.community.provider.GithubProvider;
import com.mgjt.community.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * Github初次回调
 */
@Controller
public class AuthorizeController {

    Logger logger = LoggerFactory.getLogger(AuthorizeController.class);

    @Resource
    private GithubProvider githubProvider;

    @Resource
    private UserInfo userInfo;

    @Resource
    private UserInfoService userInfoService;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.url}")
    private String redirectUrl;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code")String code,
                           @RequestParam(name = "state")String state,
                           HttpServletRequest request) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_url(redirectUrl);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser user = githubProvider.getUser(accessToken);
        if (user != null) {
            //表示登录成功，保存cookie和session
            request.getSession().setAttribute("user", user);
            //通用唯一标识码
            userInfo.setToken(UUID.randomUUID().toString());
            userInfo.setName(user.getName());
            userInfo.setAccountId(String.valueOf(user.getId()));
            userInfo.setCreateTime(System.currentTimeMillis());
            userInfo.setModifyTime(userInfo.getCreateTime());
            userInfoService.inserUserInfo(userInfo);
            System.out.println(user.getName());
            return "redirect:/";
        } else {
            //登录失败
            System.out.println(user.getName());
            return "redirect:/";
        }
    }

}
