package com.hope.projectrepository.service;

import com.hope.projectrepository.domain.entity.ProjectOverview;
import com.hope.projectrepository.util.response.json.JsonResponseWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ProjectTest {
    @Test
    public void test1(){
        List<ProjectOverview> projectOverviewList = null;

        JsonResponseWrapper jr = new JsonResponseWrapper();
        jr.addData("projectList", projectOverviewList);
    }

}
