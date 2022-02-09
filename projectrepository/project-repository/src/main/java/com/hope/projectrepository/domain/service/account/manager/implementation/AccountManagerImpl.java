package com.hope.projectrepository.domain.service.account.manager.implementation;

import com.hope.projectrepository.domain.entity.User;
import com.hope.projectrepository.domain.entity.enums.RoleType;
import com.hope.projectrepository.domain.repository.UserRepository;
import com.hope.projectrepository.domain.service.account.manager.AccountManager;
import com.hope.projectrepository.exception.service.account.*;
import com.hope.projectrepository.util.global.RandomCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AccountManagerImpl implements AccountManager {
    @Autowired
    PasswordEncoder pwEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WaitingResetPwMap waitingResetPwMap;

    @Value("${app.reset.pw.limit_time}")
    private int limitMinute;

    @Value("${app.reset.pw.length}")
    private int pwLength;

    @Value("${app.reset.pw.url.basic_url}")
    private String basicUrl;

    @Value("${app.reset.pw.url.key_length}")
    private int keyLength;

    public User createAccount(User user){
        loginIdDuplicateCheck(user);
        nicknameDuplicateCheck(user);
        User transformedUser = transformUser(user);
        userRepository.save(transformedUser);

        return transformedUser;
    }

    private void loginIdDuplicateCheck(User user){
        String loginId = user.getLoginId();
        if(userRepository.findByLoginId(loginId) != null)
            throw new LoginIdAlreadyExistException();
    }

    private void nicknameDuplicateCheck(User user){
        String nickname = user.getNickname();
        if(userRepository.findByNickname(nickname) != null)
            throw new NicknameAlreadyExistException();
    }

    private User transformUser(User user){
        RoleType roleType = getDefaultRoletype();
        String encodedPassword = getEncodePassword(user.getPassword());

        User transformedUser = User.builder()
                .loginId(user.getLoginId())
                .password(encodedPassword)
                .roleType(roleType)
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();

        return transformedUser;
    }

    private String getEncodePassword(String password){
        return pwEncoder.encode(password);
    }

    private RoleType getDefaultRoletype(){
        return RoleType.NORMAL_USER;
    }

    public void deleteAccount(User user){
        User realUser = getRealUser(user);
        userExistCheck(realUser);
        userRepository.delete(realUser);
    }

    private User getRealUser(User user){
        User realUser = userRepository.findByLoginId(user.getLoginId());
        return realUser;
    }

    private void userExistCheck(User user){
        if(user == null)
            throw new AccountDoesNotExistException();
    }

    public List<String> findAccountIdByEmail(String email){
        List<User> users = userRepository.findByEmail(email);
        List<String> ids = extractIdFromUser(users);
        return ids;
    }

    public List<String> extractIdFromUser(List<User> users){
        List<String> ids = new ArrayList<>();
        for(User user: users){
            ids.add(user.getLoginId());
        }
        return ids;
    }

    public User checkAccount(String email, String loginId){
        return userRepository.findByEmailAndLoginId(email, loginId);
    }

    public User findUserByLoginId(String loginId){
        return userRepository.findByLoginId(loginId);
    }

    public String changeAccountStateToResetPw(User user){
        LocalDateTime limit = LocalDateTime.now().plusMinutes(limitMinute);

        String resetKey = createResetKey();
        waitingResetPwMap.put(resetKey, new ResetPwInfo(user, limit));

        return getResetUrl(user.getLoginId(), resetKey);
    }
    
    public String createResetKey(){
        String key;
        do{
            key = RandomCode.getRandomCode(keyLength);
        }while(waitingResetPwMap.containsKey(key));
        return key;
    }

    public String getResetUrl(String loginId, String resetKey){
        return basicUrl + "/" + loginId + "/" + resetKey;
    }

    public String resetPw(String resetKey){
        ResetPwInfo info = checkOnReset(resetKey);
        User user = info.getUser();

        String newPw = RandomCode.getRandomCode(pwLength);
        user.setPassword(pwEncoder.encode(newPw));
        userRepository.save(user);

        return newPw;
    }

    //    파라미터 Key로 변경
    private ResetPwInfo checkOnReset(String resetKey){
        ResetPwInfo info = waitingResetPwMap.get(resetKey);
        waitingResetPwMap.remove(resetKey);

        if(info == null) throw new ResetDoesNotExistException();
        if(LocalDateTime.now().isAfter(info.getLimit())) throw new ResetTimeOutException();

        return info;
    }

    public User getUserByLoginId(String loginId){
        return userRepository.findByLoginId(loginId);
    }

    public User getUserByNickname(String nickname){
        return userRepository.findByNickname(nickname);
    }
}
