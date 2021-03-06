package com.hope.projectrepository.config.web;

import static com.hope.projectrepository.domain.entity.enums.RoleType.NORMAL_USER;
import static com.hope.projectrepository.domain.entity.enums.RoleType.ADMIN;

import com.hope.projectrepository.domain.service.login.LoginService;
import com.hope.projectrepository.exception.service.login.RedirectException;
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

    @Autowired
    LoginService loginService;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SpringSecurityDialect getSpringSecurityDialect(){ return new SpringSecurityDialect(); }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(loginService.getUserDetailService()).passwordEncoder(getPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers(publicPath).permitAll()
                .anyRequest().hasAnyAuthority(NORMAL_USER.getRoleType(), ADMIN.getRoleType())
            .and()
                .oauth2Login()
                .loginPage("/login")
                .successHandler(new LoginSuccessHandler("/", OAUTH2))
                .failureUrl("/login/fail")
            .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(new LoginSuccessHandler("/", FORM))
                .failureUrl("/login/fail")
                .permitAll()
            .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
            .and()
                .exceptionHandling().accessDeniedPage("/exception/denied")
        ;
    }

    class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
        private String loginType;

        public LoginSuccessHandler(String defaultTargetUrl, String loginType){
            this.loginType = loginType;
            setDefaultTargetUrl(defaultTargetUrl);
        }

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
            registerUser();
            setRedirection(request, response, auth);
        }

        private void registerUser(){
            if(loginType.equals(FORM))
                loginService.registerNormalUser();
            else if(loginType.equals(OAUTH2))
                loginService.registerSocialUser();
        }

        private void setRedirection(HttpServletRequest request, HttpServletResponse response, Authentication auth){
            HttpSession session = request.getSession();
            String redirectUrl = null;
            if(session != null)
                redirectUrl = (String) session.getAttribute("prevPage");

            try {
                if (session != null && redirectUrl != null) {
                    session.removeAttribute("prevPage");
                    getRedirectStrategy().sendRedirect(request, response, redirectUrl);
                } else {
                    super.onAuthenticationSuccess(request, response, auth);
                }
            }catch(Exception e){
                throw new RedirectException();
            }
        }
    }
}
