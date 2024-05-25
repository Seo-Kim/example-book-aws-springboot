package com.seok.example.book_aws.web;

import com.seok.example.book_aws.service.post.PostService;
import com.seok.example.book_aws.web.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostApiController {
    private final PostService postService;

    @PostMapping( "/api/v1/posts" )
    public Long save( @RequestBody PostDto.SaveRequest requestDto ) {
        return postService.save( requestDto );
    }

    @PutMapping( "/api/v1/posts/{id}" )
    public Long update( @PathVariable Long id, @RequestBody PostDto.UpdateRequest reqeustDto ) {
        return postService.update( id, reqeustDto );
    }

    @GetMapping( "/api/v1/posts/{id}" )
    public PostDto.Select findById( @PathVariable Long id ) {
        return postService.findById( id );
    }
}
