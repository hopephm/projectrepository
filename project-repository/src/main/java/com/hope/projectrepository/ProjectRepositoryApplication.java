package com.hope.projectrepository;

import com.hope.projectrepository.domain.User;
import com.hope.projectrepository.domain.enums.RoleType;
import com.hope.projectrepository.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.hope.projectrepository")
@SpringBootApplication
public class ProjectRepositoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectRepositoryApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner runner(UserRepository userRepository){
//		return (args) -> {
//			User user = userRepository.save(
//					User.builder()
//					.loginId("hopephm")
//					.password("q1w2e3r4")
//					.roleType(RoleType.NORMAL_USER)
//					.email("hopephm@naver.com")
//					.nickname("감자골청년")
//					.build()
//			);
//		};
//	}
}
