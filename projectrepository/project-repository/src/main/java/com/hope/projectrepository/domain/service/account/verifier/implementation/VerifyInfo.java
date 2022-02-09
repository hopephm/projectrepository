package com.hope.projectrepository.domain.service.account.verifier.implementation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Setter
@Getter
public class VerifyInfo{
    String code;
    LocalDateTime limit;
}
