package com.seok.example.book_aws.service.post;

import com.seok.example.book_aws.domain.posts.PostEntity;
import com.seok.example.book_aws.domain.posts.PostRepository;
import com.seok.example.book_aws.web.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;

    @Transactional
    public Long save( PostDto.SaveRequest requestDto ) {
        return postRepository.save( requestDto.toEntity() )
                .getId();
    }

    @Transactional
    public Long update( Long id, PostDto.UpdateRequest requestDto ) {
        PostEntity entity = postRepository.findById( id )
                .orElseThrow( () -> new IllegalArgumentException( "게시글을 찾을 수 없습니다 (id: " + id + ")" ) );
        return entity.update( requestDto.getTitle(), requestDto.getContent() );
    }

    public PostDto.Select findById( Long id ) {
        PostEntity entity = postRepository.findById( id )
                .orElseThrow( () -> new IllegalArgumentException( "게시글을 찾을 수 없습니다 (id: " + id + ")" ) );
        return new PostDto.Select( entity );
    }
}