package com.hope.projectrepository.util;

import com.hope.projectrepository.domain.entity.ProjectOverview;
import com.hope.projectrepository.domain.service.account.AccountService;
import com.hope.projectrepository.util.response.json.JsonResponseWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class JsonTests {
    @Autowired
    AccountService accountService;

    @Test
    public void jsonTest(){
        List<String> strList = new ArrayList<String>();
        strList.add("str impl");

        List<ProjectOverview> pol = new ArrayList<ProjectOverview>();
        pol.add(new ProjectOverview());

        JsonResponseWrapper jr = new JsonResponseWrapper();
        jr.addData("strList", strList);
        jr.addData("poList", pol);

        System.out.println(jr.getJsonObj());
    }

    @Test
    public void Test2(){
        List<String> idList = accountService.findLoginIdsByEmail("hopephm@naver.com");
        System.out.println(idList.toString());
    }
}
