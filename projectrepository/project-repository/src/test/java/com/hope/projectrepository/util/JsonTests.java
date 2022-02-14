package com.hope.projectrepository.util;

import com.hope.projectrepository.domain.entity.ProjectOverview;
import com.hope.projectrepository.domain.entity.User;
import com.hope.projectrepository.domain.repository.ProjectOverviewRepository;
import com.hope.projectrepository.domain.service.account.AccountService;
import com.hope.projectrepository.exception.ExceptionWrapper;
import com.hope.projectrepository.exception.service.account.AccountDoesNotExistException;
import com.hope.projectrepository.util.response.json.JsonResponseWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class JsonTests {
    @Autowired
    AccountService accountService;

    @Autowired
    ProjectOverviewRepository projectOverviewRepository;

    @Test
    public void jsonTest(){
        List<String> strList = new ArrayList<String>();
        strList.add("str impl");

        List<ProjectOverview> pol = new ArrayList<ProjectOverview>();
        pol.add(new ProjectOverview());

        JsonResponseWrapper jr = new JsonResponseWrapper();
        jr.addData("strList", strList);
        jr.addData("poList", pol);

        System.out.println(jr.getResponse());
    }

    @Test
    public void Test2(){
        Optional<ProjectOverview> po = projectOverviewRepository.findById(Long.parseLong("123"));
        if(po.equals(Optional.empty())){
            po = null;
        }
        System.out.println(po);
    }
}
