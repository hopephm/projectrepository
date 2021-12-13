package com.hope.projectrepository.domain.service.account.implementation;

import com.hope.projectrepository.domain.entity.FileInfo;
import com.hope.projectrepository.domain.entity.ProjectContent;
import com.hope.projectrepository.domain.entity.ProjectOverview;
import com.hope.projectrepository.domain.entity.User;
import com.hope.projectrepository.domain.entity.enums.RoleType;
import com.hope.projectrepository.domain.repository.FileInfoRepository;
import com.hope.projectrepository.domain.repository.ProjectContentRepository;
import com.hope.projectrepository.domain.repository.ProjectOverviewRepository;
import com.hope.projectrepository.domain.repository.UserRepository;
import com.hope.projectrepository.domain.service.mail.MailService;
import com.hope.projectrepository.domain.service.verification.VerificationService;
import com.hope.projectrepository.util.RandomCode;
import com.hope.projectrepository.util.Result;
import com.hope.projectrepository.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LagacyAccountServiceImpl {
    @Value("${app.reset.pw.length}")
    private int pwLength;
    @Value("${app.reset.pw.limit_time}")
    private int limitTime;

    @Autowired UserRepository userRepository;
    @Autowired ProjectOverviewRepository projectOverviewRepository;
    @Autowired ProjectContentRepository projectContentRepository;
    @Autowired FileInfoRepository fileInfoRepository;

    @Autowired
    MailService mailService;
    @Autowired
    VerificationService verificationService;

    @Autowired PasswordEncoder pwEncorder;

    // 주기적으로 비우는 batch 추가
    private static Map<String, LocalDateTime> accountResetStrorage = new HashMap<>();

    @Transactional
    public Long createAccount(String loginId, String password, String email, String nickname){
        // 실패시, Exception
        
        
//        아이디 길이: js 단

//        아이디 중복: js 단

//        이메일 인증: js 단

//        닉네임 중복: js 단

        String encordedPassword = pwEncorder.encode(password);

        User user = User.builder()
                .loginId(loginId)
                .password(encordedPassword)
                .email(email)
                .roleType(RoleType.NORMAL_USER)
                .nickname(nickname)
                .build();

        if(null == userRepository.findByLoginId(loginId))
            userRepository.save(user);

        return user.getUserId();
    }

    @Transactional
    public String deleteAccount(HttpServletRequest request, HttpServletResponse response){
        User user = Util.getCurrentUser();
        if(user == null) return Result.FAIL;

        user = userRepository.findByUserId(user.getUserId());
        List<ProjectOverview> overviews = projectOverviewRepository.findByUser(user);

        for(ProjectOverview overview: overviews){
            List<ProjectContent> contents = projectContentRepository.findByProjectOverview(overview);

            for(ProjectContent content: contents){
                FileInfo file = fileInfoRepository.findByProjectContent(content);
                if(file != null) fileInfoRepository.delete(file);
                if(content != null) projectContentRepository.delete(content);
            }
            projectOverviewRepository.delete(overview);
        }

        userRepository.delete(user);
        Util.logout(request, response);

        return Result.SUCCESS;
    }

    public List<String> findAccountIdByEmail(String email){
        List<User> users = userRepository.findByEmail(email);

        List<String> ids = new ArrayList<>();

        for(User user: users){
            ids.add(user.getLoginId());
        }

        return ids;
    }

    public String sendVerificationCode(String email, String key){
        String randomCode = verificationService.createRandomCode(key);
        mailService.sendVerificationMail(email, randomCode);

        return randomCode;
    }

    public String verifyCode(String email, String code){
        return verificationService.verifyRandomCode(email, code);
    }



    public String checkAccount(String loginId, String email){
        User user = userRepository.findByLoginId(loginId);
//            계정정보 불일치 예외 핸들링
        if(user == null) return Result.FAIL;
        if(user.getEmail().equals(email)) return Result.SUCCESS;
        return Result.FAIL;
    }

    public void addResetAccount(String loginId){
        accountResetStrorage.put(loginId, LocalDateTime.now().plusMinutes(limitTime));
    }

    public String resetPw(String loginId, String email){
//        비밀번호 변경 대기상태 검증
        LocalDateTime limit = accountResetStrorage.get(loginId);

        if(limit == null) return Result.FAIL;
        if(LocalDateTime.now().isAfter(limit)){
            accountResetStrorage.remove(loginId);
            return Result.FAIL;
        }
        if(LocalDateTime.now().isBefore(limit)){
            accountResetStrorage.remove(loginId);

            String newPw = RandomCode.getRandomCode(pwLength);

            User user = userRepository.findByLoginId(loginId);
            user.setPassword(pwEncorder.encode(newPw));
            userRepository.save(user);

            mailService.sendNewPassword(loginId, email, newPw);

            return Result.SUCCESS;
        }

        return Result.FAIL;
    }

}
