package com.hope.projectrepository.filter;

import com.hope.projectrepository.domain.entity.User;
import com.hope.projectrepository.domain.entity.enums.RoleType;
import com.hope.projectrepository.domain.repository.UserRepository;
import com.hope.projectrepository.util.global.ContextManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// 이건 config쪽으로 옮기는게 맞을듯


@Service
public class LoginFilter implements UserDetailsService {
    private static final String FACEBOOK = "facebook";
    private static final String GOOGLE = "google";
    private static final String NAVER = "naver";
    private static final String KAKAO = "kakao";

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException{
        User user = userRepository.findByLoginId(loginId);

        String username = "";
        String password = "";
        List<GrantedAuthority> auth = new ArrayList<>();

        try{
            username = user.getLoginId();
            password = user.getPassword();
            auth.add(new SimpleGrantedAuthority(RoleType.NORMAL_USER.getRoleType()));
        }catch(UsernameNotFoundException e){
            throw new UsernameNotFoundException("Not registered User");
        }

        return new org.springframework.security.core.userdetails.User(username, password, auth);
    }

    @Transactional
    public User registerNormalUser(){
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.core.userdetails.User userToken =
                (org.springframework.security.core.userdetails.User)authToken.getPrincipal();

        User user = userRepository.findByLoginId(userToken.getUsername());

        this.saveUserInfoToSession(user);

        return user;
    }

    @Transactional
    public User registerSocialUser(){
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> userInfo = authToken.getPrincipal().getAttributes();

        User tmpUser = convertUser(authToken.getAuthorizedClientRegistrationId(), userInfo);

//        해당 소셜 email을 LoginId로 하고 실제 회원가입 및 로그인 시에는 이메일 형태를 입력받을 수 없게하자
//        마찬가지 패스워드도 공백을 입력할 수 없게 하자
        User user = userRepository.findByLoginId(tmpUser.getLoginId());
        if(user == null) {
            user = tmpUser;
            userRepository.save(tmpUser);
        }

        // OAuth2 인증토큰에서 현재 앱의 인증토큰 형태로 통일
        this.changeAuthoriy(user, userInfo, authToken);
        this.saveUserInfoToSession(user);

        return user;
    }

    private User convertUser(String roleName, Map<String, Object> userInfo) {
        String nickName = "";
        String loginId = "{" + roleName + "}";
        String email = "";

        if(roleName.equals(FACEBOOK) || roleName.equals(GOOGLE)){
            nickName = String.valueOf(userInfo.get("name"));
            loginId += String.valueOf(userInfo.get("email"));
            email = String.valueOf(userInfo.get("email"));

        }else if(roleName.equals(NAVER)){
            Map<String, String> uInfo = (Map<String, String>) userInfo.get("response");

            nickName = String.valueOf(uInfo.get("nickname"));
            loginId += String.valueOf(uInfo.get("email"));
            email = String.valueOf(uInfo.get("email"));
        }else if(roleName.equals(KAKAO)){
            Map<String, Object> uInfo = (Map<String, Object>) userInfo.get("kakao_account");
            Map<String, String> pro = (Map<String, String>) uInfo.get("profile");

            nickName = String.valueOf(pro.get("nickname"));
            loginId += String.valueOf(uInfo.get("email"));
            email = String.valueOf(uInfo.get("email"));
        }

        User user = User.builder()
                    .nickname(nickName)
                    .loginId(loginId)
                    .email(email)
                    .roleType(RoleType.NORMAL_USER)
                    .build();

        return user;
    }

    private void changeAuthoriy(User user, Map<String, Object> userInfo, OAuth2AuthenticationToken authToken){
        if(!authToken.getAuthorities().contains(new SimpleGrantedAuthority(user.getRoleType().getRoleType()))){
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userInfo, "N/A", AuthorityUtils.createAuthorityList(user.getRoleType().getRoleType())));
        }
    }

    private void saveUserInfoToSession(User user){
        HttpSession session = ContextManager.getCurrentSession();
        session.setAttribute("user", user);
    }
}
