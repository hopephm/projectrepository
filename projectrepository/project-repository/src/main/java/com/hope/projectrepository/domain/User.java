package com.hope.projectrepository.domain;

import com.hope.projectrepository.domain.enums.RoleType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity(name = "User")
@Table(name = "user")
public class User {
    @Id
    @Column
    @GeneratedValue
    private Long userId;

    @Column private RoleType roleType;

    @Column private String loginId;
    @Column private String password;
    @Column private String email;
    @Column private String nickname;

    @Builder
    public User(Long userId, String loginId, String password, String email, String nickname, RoleType roleType){
        this.userId = userId;
        this.roleType = roleType;
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
    }
}
