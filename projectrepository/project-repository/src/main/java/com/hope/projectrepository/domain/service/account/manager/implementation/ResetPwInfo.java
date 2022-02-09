package com.hope.projectrepository.domain.service.account.manager.implementation;

import com.hope.projectrepository.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Setter
@Getter
public class ResetPwInfo {
    private User user;
    private LocalDateTime limit;
}
