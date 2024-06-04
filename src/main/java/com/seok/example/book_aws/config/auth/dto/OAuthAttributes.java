package com.seok.example.book_aws.config.auth.dto;

import com.seok.example.book_aws.entity.user.Role;
import com.seok.example.book_aws.entity.user.Users;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private String nameAttrKey;
    private Map< String, Object > attributes;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes( String nameAttrKey, Map< String, Object > attributes,
                            String name, String email, String picture ) {
        this.nameAttrKey = nameAttrKey;
        this.attributes = attributes;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    /**
     * 로그인 서비스마다 다른 반환 정보들을 통합
     *
     * @param attributes OAuth2User 반환 정보
     */
    public static OAuthAttributes of( String userNameAttrKey, Map< String, Object > attributes ) {
        return ofGoogle( userNameAttrKey, attributes );
    }

    private static OAuthAttributes ofGoogle( String userNameAttrKey, Map< String, Object > attributes ) {
        return OAuthAttributes.builder()
                .nameAttrKey( userNameAttrKey )
                .attributes( attributes )
                .name( (String) attributes.get( "name" ) )
                .email( (String) attributes.get( "email" ) )
                .picture( (String) attributes.get( "picture" ) )
                .build();
    }

    public Users toEntity() {
        return Users.builder()
                .name( this.name )
                .email( this.email )
                .picture( this.picture )
                .role( Role.GUEST )  // 신규 가입 시 권한
                .build();
    }
}
