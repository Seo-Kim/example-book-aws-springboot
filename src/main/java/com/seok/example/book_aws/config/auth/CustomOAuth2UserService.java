package com.seok.example.book_aws.config.auth;

import com.seok.example.book_aws.config.auth.dto.OAuthAttributes;
import com.seok.example.book_aws.config.auth.dto.SessionUser;
import com.seok.example.book_aws.entity.user.UserRepository;
import com.seok.example.book_aws.entity.user.Users;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser( OAuth2UserRequest userRequest ) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser( userRequest );
        // 로그인 서비스 구분 코드 (google, naver, kakao 등)
        //String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // 로그인 PK가 될 필드
        String userNameAttrKey = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        OAuthAttributes attributes = OAuthAttributes.of( userNameAttrKey, oAuth2User.getAttributes() );

        Users users = saveOrUpdate( attributes );
        httpSession.setAttribute( "user", new SessionUser( users ) );

        return new DefaultOAuth2User(
                Collections.singleton( new SimpleGrantedAuthority( users.getRoleKey() ) ),
                attributes.getAttributes(),
                attributes.getNameAttrKey()
        );
    }

    private Users saveOrUpdate( OAuthAttributes attributes ) {
        Users users = userRepository.findByEmail( attributes.getEmail() )
                .map( entity -> entity.update( attributes.getName(), attributes.getPicture() ) )
                .orElse( attributes.toEntity() );
        return userRepository.save( users );
    }
}