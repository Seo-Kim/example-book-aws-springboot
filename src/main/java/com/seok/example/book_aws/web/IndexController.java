package com.seok.example.book_aws.web;

import com.seok.example.book_aws.config.auth.dto.SessionUser;
import com.seok.example.book_aws.service.post.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final PostService postService;
    private final HttpSession httpSession;

    @GetMapping( "/" )
    public String index( Model model ) {
        model.addAttribute( "posts", postService.findAllDesc() );

        SessionUser user = (SessionUser) httpSession.getAttribute( "user" );
        if( user != null )
            model.addAttribute( "userName", user.getName() );

        return "index";
    }

    @GetMapping( "/posts/save" )
    public String postSave() {
        return "post-save";
    }

    @GetMapping( "/posts/update/{id}" )
    public String postUpdate( @PathVariable long id, Model model ) {
        model.addAttribute( "post", postService.findById( id ) );
        return "post-update";
    }
}
