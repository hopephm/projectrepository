package com.hope.projectrepository.util;


public class RandomCode {
    // 코드허용 문자 관리를 위해 단순 ASCII 코드로 하지말자
    private static final Character[] allowedCharacter = {
            '1','2','3','4','5','6','7','8','9','0',
            'a', 'b', 'c', 'd', 'e', 'f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
            'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
    };

    private static int getRandomNum(int range){
        return (int)(Math.random()*range);
    }

    public static String getRandomCode(int length){
        String randomCode = "";
        
        for(int i = 0; i < length; i++)
            randomCode += allowedCharacter[getRandomNum(allowedCharacter.length)];

        return randomCode;
    }

}
