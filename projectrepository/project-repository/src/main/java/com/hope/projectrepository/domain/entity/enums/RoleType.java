package com.hope.projectrepository.domain.entity.enums;


public enum RoleType {
    NORMAL_USER("NORMAL_USER"),
    ADMIN("ADMIN");
    
    private String role;
    private static final String ROLE_PREFIX = "ROLE_";
    
    RoleType(String role){
        this.role = role;
    }
    
//    public String getRoleName(){
//        return  this.role;
//    }
    public String getRoleType() { return ROLE_PREFIX + this.role;}
}
