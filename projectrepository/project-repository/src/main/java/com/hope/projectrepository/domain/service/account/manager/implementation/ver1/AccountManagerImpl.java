package com.hope.projectrepository.domain.service.account.manager.implementation.ver1;

import com.hope.projectrepository.domain.entity.User;
import com.hope.projectrepository.domain.entity.enums.RoleType;
import com.hope.projectrepository.domain.repository.UserRepository;
import com.hope.projectrepository.domain.service.account.manager.AccountManager;
import com.hope.projectrepository.util.RandomCode;
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
    private int limitTime;

    @Value("${app.reset.pw.length}")
    private int pwLength;

    public User createAccount(User user){
        String encodedPassword = getEncodePassword(user.getPassword());
        RoleType roleType = getDefaultRoletype();

        User transformedUser = User.builder()
                                    .loginId(user.getLoginId())
                                    .password(encodedPassword)
                                    .roleType(roleType)
                                    .email(user.getEmail())
                                    .nickname(user.getNickname())
                                    .build();
        userRepository.save(transformedUser);

        return transformedUser;
    }

    private String getEncodePassword(String password){
        return pwEncoder.encode(password);
    }

    private RoleType getDefaultRoletype(){
        return RoleType.NORMAL_USER;
    }

    public void deleteAccount(User user){
        User realUser = userRepository.findByLoginId(user.getLoginId());
        userRepository.delete(realUser);
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

    public String resetPassword(User user) throws Exception{
        checkOnReset(user);
        String newPw = RandomCode.getRandomCode(pwLength);
        user.setPassword(pwEncoder.encode(newPw));
        userRepository.save(user);
        return newPw;
    }

    private void checkOnReset(User user) throws Exception{
        LocalDateTime changeTime = waitingResetPwMap.get(user);
        waitingResetPwMap.remove(user);

        if(changeTime == null) throw new Exception();
        if(LocalDateTime.now().isAfter(changeTime)) throw new Exception();
    }

    public void putAccountToWaitingResetPwMap(User user){
        waitingResetPwMap.put(user, LocalDateTime.now().plusMinutes(limitTime));
    }
}
