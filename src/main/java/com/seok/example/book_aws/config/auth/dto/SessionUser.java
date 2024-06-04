package com.seok.example.book_aws.config.auth.dto;

import com.seok.example.book_aws.entity.user.Users;
import lombok.Getter;

import java.io.Serializable;

@Getter
/** 인증한 사용자 정보를 세션에 저장하려면 직렬화되어 있어야 하기에 Entity 직접 쓰기엔 위험 부담 존재 */
public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String picture;

    public SessionUser( Users users ) {
        this.name = users.getName();
        this.email = users.getEmail();
        this.picture = users.getPicture();
    }
}
