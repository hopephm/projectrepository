package com.hope.projectrepository.util.global;

public enum Result {
    UnKnownError("999", "UnKnown"),
    OK("000", "OK");

    private String code;
    private String description;

    Result(String code, String description){
        this.code =code;
        this.description = description;
    }

    public String getCode(){
        return this.code;
    }

    public String getDescription(){
        return this.description;
    }
}
