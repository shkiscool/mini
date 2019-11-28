package com.dazhao.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dazhao.App;
import com.dazhao.common.result.CommonResult;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = {App.class})
@AutoConfigureMockMvc
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static String token = null;

    /**
     * 登录测试
     */
    @Before
    public void login() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("username", "sadmin");
        map.put("password", "123456");
        String content = JSONObject.toJSONString(map);

        String str = mockMvc.perform(MockMvcRequestBuilders.post("/user/auth")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(content)).andReturn().getResponse().getContentAsString();
        CommonResult parse = JSON.parseObject(str, CommonResult.class);
        Map<String, Object> data = (Map<String, Object>) parse.getData();
        token = (String) data.get("token");
    }

    @Test
    public void createAdmin() {

    }

    @Test
    public void updateAdmin() {

    }

    @Test
    public void getlist() {
    }

    @Test
    public void getAdminList() throws Exception {
        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.get("/user/admin_list").header("authorization", token)).andReturn()
                .getResponse().getContentAsString();
        System.out.println(contentAsString);
    }

    @Test
    public void deletedAdmin() {
    }
}