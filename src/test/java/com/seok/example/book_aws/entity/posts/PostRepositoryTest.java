package com.seok.example.book_aws.entity.posts;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith( SpringExtension.class )
@SpringBootTest  // H2 DB 실행 목적
class PostRepositoryTest {
    @Autowired
    PostRepository postRepository;

    @AfterEach  // Each: 각 테스트 메서드 실행 후 매번 실행, All: 모든 테스트 메서드 실행 후 한 번 실행 (반드시 static)
    public void cleanUp() {
        postRepository.deleteAll();
    }

    @Test
    public void 저장하고_불러오기() {
        String title = "제목 테스트";
        String content = "본문 내용 테스트";
        String author = "admin@seok.com";
        Post reqPost = Post.builder()
                .title( title )
                .content( content )
                .author( author )
                .build();

        postRepository.save( reqPost );
        List< Post > postList = postRepository.findAll();

        Post resPost = postList.get( 0 );
        assertEquals( title, resPost.getTitle() );
        assertEquals( content, resPost.getContent() );
        assertEquals( reqPost, resPost );  // equals() 정의로 동일 id는 같은 객체로 봄
    }
}