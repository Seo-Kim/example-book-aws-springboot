package com.seok.example.book_aws.web;

import com.seok.example.book_aws.config.auth.dto.SessionUser;
import com.seok.example.book_aws.config.web.LoginUser;
import com.seok.example.book_aws.entity.user.Role;
import com.seok.example.book_aws.service.post.PostService;
import com.seok.example.book_aws.web.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final PostService postService;

    @GetMapping( "/" )
    public String index( @LoginUser SessionUser user, Model model ) {
        model.addAttribute( "posts", postService.findAllDesc() );

        // ArgumentResolver로 재사용
        //SessionUser user = (SessionUser) httpSession.getAttribute( "user" );
        if( user != null )
            model.addAttribute( "userName", user.getName() );

        return "index";
    }

    @GetMapping( "/posts/save" )
    public String postSave( @LoginUser SessionUser user, Model model ) {
        if( user == null )
            throw new IllegalArgumentException( "잘못된 접근" );
        model.addAttribute( "user", user );

        return "post-save";
    }

    @GetMapping( "/posts/update/{id}" )
    public String postUpdate( @PathVariable long id, @LoginUser SessionUser user, Model model ) {
        PostDto.Select post = postService.findById( id );
        model.addAttribute( "post", post );

        // 작성자 또는 관리자만 수정 가능
        if( user == null
                || ( post.getCreateId().compareTo( user.getId() ) != 0 && !Role.ADMIN.getKey().equals( user.getRole() ) ) )
            throw new AuthorizationServiceException( "권한이 없습니다" );
        model.addAttribute( "userId", user.getId() );

        return "post-update";
    }
}