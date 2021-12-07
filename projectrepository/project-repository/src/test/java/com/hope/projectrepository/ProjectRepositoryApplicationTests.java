package com.hope.projectrepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hope.projectrepository.domain.ProjectOverview;
import com.hope.projectrepository.domain.User;
import com.hope.projectrepository.dto.ProjectDTO;
import com.hope.projectrepository.dto.ContentDTO;
import com.hope.projectrepository.repository.ProjectOverviewRepository;
import com.hope.projectrepository.repository.UserRepository;
import com.hope.projectrepository.service.MailService;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.swing.text.AbstractDocument;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ProjectRepositoryApplicationTests {
	@Autowired
	MailService mailService;

	@Test
	public void test(){
//		mailService.sendRandomCode("hopephm@naver.com","123456");
	}

}
