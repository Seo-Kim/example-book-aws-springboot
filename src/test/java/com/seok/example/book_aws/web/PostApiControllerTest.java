package com.seok.example.book_aws.web;

import com.seok.example.book_aws.entity.posts.Post;
import com.seok.example.book_aws.entity.posts.PostRepository;
import com.seok.example.book_aws.web.dto.PostDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith( SpringExtension.class )
// 외부 연동 기능 외에도 JPA 기능 테스트가 필요하므로 @WebMvcTest 대신 @SpringBootTest와 TestRestTemplate 사용
@SpringBootTest( webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT )
class PostApiControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private PostRepository postRepository;

    @AfterEach
    void tearDown() throws Exception {
        postRepository.deleteAll();
    }

    @Test
    void 등록_정상() throws Exception {
        String url = "http://localhost:" + port + "/api/v1/posts";

        // 저장
        String title = "테스트 제목";
        String content = "테스트 본문";
        String author = "Admin SeoK";
        PostDto.SaveRequest requestSaveDto = PostDto.SaveRequest.builder()
                .title( title )
                .content( content )
                .author( author )
                .build();

        ResponseEntity< Long > responseEntity = restTemplate.postForEntity( url, requestSaveDto, Long.class );

        assertEquals( HttpStatus.OK, responseEntity.getStatusCode() );
        assertTrue( responseEntity.getBody() > 0L );

        // 단일 조회
        Long savedId = responseEntity.getBody();

        ResponseEntity< PostDto.Select > responseSelectEntity = restTemplate.getForEntity( url + "/" + savedId, PostDto.Select.class );

        assertEquals( HttpStatus.OK, responseSelectEntity.getStatusCode() );
        PostDto.Select selectDto = responseSelectEntity.getBody();
        assertEquals( title, selectDto.getTitle() );
        assertEquals( content, selectDto.getContent() );
        assertEquals( author, selectDto.getAuthor() );

        // 수정
        String updateTitle = "테스트 수정 제목";
        String updateContent = "테스트 수정 본문";
        PostDto.UpdateRequest requestUpdateDto = PostDto.UpdateRequest.builder()
                .title( updateTitle )
                .content( updateContent )
                .build();

        HttpEntity< PostDto.UpdateRequest > requestUpdateEntity = new HttpEntity<>( requestUpdateDto );
        responseEntity = restTemplate.exchange( url + "/" + savedId, HttpMethod.PUT, requestUpdateEntity, Long.class );

        assertEquals( HttpStatus.OK, responseEntity.getStatusCode() );
        assertTrue( responseEntity.getBody() > 0L );
        assertEquals( savedId, responseEntity.getBody() );

        // 전체 조회
        List< Post > all = postRepository.findAll();

        assertEquals( HttpStatus.OK, responseSelectEntity.getStatusCode() );
        assertEquals( 1, all.size() );

        Post selectEntity = all.get( 0 );
        assertEquals( savedId, selectEntity.getId() );
        assertEquals( updateTitle, selectEntity.getTitle() );
        assertEquals( updateContent, selectEntity.getContent() );
        assertEquals( author, selectEntity.getAuthor() );
        System.out.println( "\n" + selectEntity.getCreatedDate() + "\n" + selectEntity.getModifiedDate() );
    }
}