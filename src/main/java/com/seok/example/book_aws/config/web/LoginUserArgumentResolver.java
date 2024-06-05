package com.seok.example.book_aws.config.web;

import com.seok.example.book_aws.config.auth.dto.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 세션 User 정보 불러와 파라미터에 연결
 *
 * @see LoginUser
 */
@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final HttpSession httpSession;

    @Override
    public boolean supportsParameter( MethodParameter parameter ) {
        boolean isAnnotation = parameter.getParameterAnnotation( LoginUser.class ) != null;
        boolean isUserClass = SessionUser.class.equals( parameter.getParameterType() );
        return isAnnotation && isUserClass;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {
        return httpSession.getAttribute( "user" );
    }
}