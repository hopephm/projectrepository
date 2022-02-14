package com.hope.projectrepository.domain.service.login.implementation;

import com.hope.projectrepository.domain.entity.User;
import com.hope.projectrepository.domain.entity.enums.RoleType;
import com.hope.projectrepository.domain.repository.UserRepository;
import com.hope.projectrepository.domain.service.login.LoginService;
import com.hope.projectrepository.exception.service.account.AccountDoesNotExistException;
import com.hope.projectrepository.exception.service.account.UnknownRoleTypeException;
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

public class LoginServiceImpl implements LoginService{
    private static final String FACEBOOK = "facebook";
    private static final String GOOGLE = "google";
    private static final String NAVER = "naver";
    private static final String KAKAO = "kakao";

    @Autowired
    UserRepository userRepository;

    public UserDetailsService getUserDetailService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String loginId){
                UserDetails userDetails = null;
                try{
                    User user = getUserByLoginId(loginId);
                    userDetails = userNormalLogin(user);
                }catch(UsernameNotFoundException e){
                    throw new AccountDoesNotExistException();
                }
                return userDetails;
            }
        };
    }

    private org.springframework.security.core.userdetails.User userNormalLogin(User user){
        String username = user.getLoginId();
        String password = user.getPassword();

        List<GrantedAuthority> auth = new ArrayList<>();
        auth.add(new SimpleGrantedAuthority(RoleType.NORMAL_USER.getRoleType()));

        return new org.springframework.security.core.userdetails.User(username, password, auth);
    }

    private User getUserByLoginId(String loginId){
        User user = userRepository.findByLoginId(loginId);
        if(user == null)
            throw new AccountDoesNotExistException();
        return user;
    }

    @Transactional
    public User registerNormalUser(){
        org.springframework.security.core.userdetails.User userToken = getCurrentUser();
        User user = convertUser(userToken);
        saveUserInfoToSession(user);
        return user;
    }

    private User convertUser(org.springframework.security.core.userdetails.User user){
        String loginId = user.getUsername();
        return getUserByLoginId(loginId);
    }

    private org.springframework.security.core.userdetails.User getCurrentUser(){
        UsernamePasswordAuthenticationToken authToken = getCurrentUsernamePasswordAuthentication();
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User)authToken.getPrincipal();
        return user;
    }

    private UsernamePasswordAuthenticationToken getCurrentUsernamePasswordAuthentication(){
        return (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
    }

    @Transactional
    public User registerSocialUser(){
        OAuth2AuthenticationToken authToken = getCurrentOAuth2AuthenticationToken();

        Map<String, Object> userInfo = authToken.getPrincipal().getAttributes();
        String roleName = authToken.getAuthorizedClientRegistrationId();

        User user = getAccountIfItExists(userInfo);
        if(user == null){
            user = convertUser(roleName, userInfo);
            userRepository.save(user);
        }

        changeAuthority(user, userInfo, authToken);
        saveUserInfoToSession(user);

        return user;
    }

    private OAuth2AuthenticationToken getCurrentOAuth2AuthenticationToken(){
        return (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    }

    private User getAccountIfItExists(Map<String, Object> userInfo){
        User user = userRepository.findByLoginId(String.valueOf(userInfo.get("email")));
        return user;
    }

    private User convertUser(String roleName, Map<String, Object> userInfo) {
        User user = null;

        if(isCommonRoleType(roleName)){
            user = constructCommonUser(userInfo);
        }else if(isNaverRoleType(roleName)){
            user = constructNaverUser(userInfo);
        }else if(isKakaoRoleType(roleName)){
            user = constructKakaoUser(userInfo);
        }

        if(user == null)
            throw new UnknownRoleTypeException();

        return user;
    }

    private User buildUser(String loginId, String email, String nickname){
        User user = User.builder()
                .nickname(nickname)
                .loginId(loginId)
                .email(email)
                .roleType(RoleType.NORMAL_USER)
                .build();
        return user;
    }

    private boolean isCommonRoleType(String roleName){
        if(roleName.equals(FACEBOOK) || roleName.equals(GOOGLE))
            return true;
        return false;
    }

    private User constructCommonUser(Map<String, Object> userInfo){
        String loginId = String.valueOf(userInfo.get("email"));
        String email = String.valueOf(userInfo.get("email"));
        String nickname = String.valueOf(userInfo.get("name"));
        return buildUser(loginId, email, nickname);
    }

    private boolean isNaverRoleType(String roleName){
        if(roleName.equals(NAVER))
            return true;
        return false;
    }

    private User constructNaverUser(Map<String, Object> userInfo){
        Map<String, String> naverUserInfo = (Map<String, String>) userInfo.get("response");

        String loginId = String.valueOf(naverUserInfo.get("email"));
        String email = String.valueOf(naverUserInfo.get("email"));
        String nickname = String.valueOf(naverUserInfo.get("nickname"));
        return buildUser(loginId, email, nickname);
    }

    private boolean isKakaoRoleType(String roleName){
        if(roleName.equals(KAKAO))
            return true;
        return false;
    }

    private User constructKakaoUser(Map<String ,Object> userInfo){
        Map<String, Object> info = (Map<String, Object>) userInfo.get("kakao_account");
        Map<String, String> profile = (Map<String, String>) info.get("profile");

        String loginId = String.valueOf(info.get("email"));
        String email = String.valueOf(info.get("email"));
        String nickname = String.valueOf(profile.get("nickname"));

        return buildUser(loginId, email, nickname);
    }

    private void changeAuthority(User user, Map<String, Object> userInfo, OAuth2AuthenticationToken authToken){
        if(!authToken.getAuthorities().contains(new SimpleGrantedAuthority(user.getRoleType().getRoleType()))){
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userInfo, "N/A", AuthorityUtils.createAuthorityList(user.getRoleType().getRoleType())));
        }
    }

    private void saveUserInfoToSession(User user){
        HttpSession session = ContextManager.getCurrentSession();
        session.setAttribute("user", user);
    }
}
