package com.hope.projectrepository.config;

import static com.hope.projectrepository.domain.enums.RoleType.NORMAL_USER;
import static com.hope.projectrepository.domain.enums.RoleType.ADMIN;

import com.hope.projectrepository.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final String FORM="form";
    private final String OAUTH2="oauth2";

    @Value("${app.security.path.public}")
    public String[] publicPath;

//    @Value("${app.security.path.private}")
//    public String[] privatePath;

    @Autowired
    LoginService loginService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SpringSecurityDialect springSecurityDialect(){ return new SpringSecurityDialect(); }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers(publicPath).permitAll()
//                .antMatchers(privatePath).hasAnyAuthority(NORMAL_USER.getRoleType(), ADMIN.getRoleType())
                .anyRequest().hasAnyAuthority(NORMAL_USER.getRoleType(), ADMIN.getRoleType())
            .and()
                .oauth2Login()
                .loginPage("/login")
                .successHandler(new LoginSuccessHandler("/login/success", OAUTH2)) // 로그인 성공 핸들링
                .failureUrl("/login/fail")
            .and()
                .formLogin()
                .loginPage("/login")                    // basic login url
                .loginProcessingUrl("/login")           // post로 로그인 처리할 url
                .successHandler(new LoginSuccessHandler("/login/success", FORM)) // 로그인 성공 핸들링
                .failureUrl("/login/fail")              // 로그인 실패시 redirect 될 url
                .permitAll()
            .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/logout/success")
                .invalidateHttpSession(true)
            .and()
                .exceptionHandling().accessDeniedPage("/denied")
        ;

    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(loginService).passwordEncoder(passwordEncoder());
    }

    class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
        private String loginType;

        public LoginSuccessHandler(String defaultTargetUrl, String loginType){
            this.loginType = loginType;
            setDefaultTargetUrl(defaultTargetUrl);
        }
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth)
                throws ServletException, IOException {
            if(loginType.equals(FORM))
                loginService.registerNormalUser();
            else if(loginType.equals(OAUTH2))
                loginService.registerSocialUser();

            HttpSession session = request.getSession();

            if (session != null) {
                String redirectUrl = (String) session.getAttribute("prevPage");
                if (redirectUrl != null) {
                    session.removeAttribute("prevPage");
                    getRedirectStrategy().sendRedirect(request, response, redirectUrl);
                } else {
                    super.onAuthenticationSuccess(request, response, auth);
                }
            } else {
                super.onAuthenticationSuccess(request, response, auth);
            }
        }
    }
}
