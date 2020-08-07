package com.cos.jwtex01.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String roles = "";

    public User(String username, String password, String roles){
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    protected User(){}

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRoles() {
        return roles;
    }

    //ENUM으로 안하고 ,로 해서 구분해서 ROLE을 입력 -> 그걸 파싱
    //데이터 베이스 원자성을 파괴함 (상황에 따라서 다르기 때문에)
    public List<String> getRoleList(){
        if(this.roles.length() > 0){
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }
}
