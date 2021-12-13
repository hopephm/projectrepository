package com.hope.projectrepository;

import com.hope.projectrepository.domain.service.mail.MailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProjectRepositoryApplicationTests {
	@Autowired
	MailService mailService;

	@Test
	public void test(){
//		mailService.sendRandomCode("hopephm@naver.com","123456");
	}

}
