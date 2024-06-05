package com.seok.example.book_aws.service.post;

import com.seok.example.book_aws.entity.posts.Post;
import com.seok.example.book_aws.entity.posts.PostRepository;
import com.seok.example.book_aws.web.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        Post entity = postRepository.findById( id )
                .orElseThrow( () -> new IllegalArgumentException( "게시글을 찾을 수 없습니다 (id: " + id + ")" ) );
        return entity.update( requestDto.getTitle(), requestDto.getContent(), requestDto.getModifyId() );
    }

    @Transactional( readOnly=true )
    public PostDto.Select findById( Long id ) {
        Post entity = postRepository.findById( id )
                .orElseThrow( () -> new IllegalArgumentException( "게시글을 찾을 수 없습니다 (id: " + id + ")" ) );
        return new PostDto.Select( entity );
    }

    @Transactional( readOnly=true )
    public List< PostDto.List > findAllDesc() {
        return postRepository.findAll( Sort.by( Sort.Direction.DESC, "id" ) ).stream()
                .map( PostDto.List::new )
                .collect( Collectors.toList() );
    }

    @Transactional
    public Long delete( Long id ) {
        Post entity = postRepository.findById( id )
                .orElseThrow( () -> new IllegalArgumentException( "게시글을 찾을 수 없습니다 (id: " + id + ")" ) );

        postRepository.delete( entity );
        return id;
    }
}