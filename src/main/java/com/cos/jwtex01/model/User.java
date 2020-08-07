package com.cos.jwtex01.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.*;

import lombok.Data;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String username;
    private String password;
    private String roles = "";

    //ENUM으로 안하고 ,로 해서 구분해서 ROLE을 입력 -> 그걸 파싱
    //데이터 베이스 원자성을 파괴함 (상황에 따라서 다르기 때문에)
    public List<String> getRoleList(){
        if(this.roles.length() > 0){
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }
}
