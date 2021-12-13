package com.hope.projectrepository.domain.service.account.manager;

import com.hope.projectrepository.domain.entity.User;
import com.hope.projectrepository.domain.entity.enums.RoleType;
import com.hope.projectrepository.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AccountManagerImpl implements AccountManager{
    @Autowired
    PasswordEncoder pwEncoder;

    @Autowired
    UserRepository userRepository;

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
}
