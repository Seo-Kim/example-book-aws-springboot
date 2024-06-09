package com.seok.example.book_aws.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seok.example.book_aws.entity.posts.Post;
import com.seok.example.book_aws.entity.posts.PostRepository;
import com.seok.example.book_aws.web.dto.PostDto;
import jakarta.servlet.http.HttpServletMapping;
import jakarta.servlet.http.MappingMatch;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletMapping;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith( SpringExtension.class )
// 외부 연동 기능 외에도 JPA 기능 테스트가 필요하므로 @WebMvcTest 대신 @SpringBootTest와 TestRestTemplate 사용
@SpringBootTest( webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT )
// > Security 적용으로 TestRestTemplate 대신 MockMVC, @WithMockUser와 사용
@WithMockUser( roles="USER" )
// Mock MVC 설정 (MockMvcBuilders 대체)
@AutoConfigureMockMvc
class PostApiControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private PostRepository postRepository;

    /* MockMvc로 대체
    @Autowired
    private TestRestTemplate restTemplate;*/
    /* @AutoConfigureMockMvc로 대체
    @Autowired
    private WebApplicationContext context;*/
    @Autowired  // annotation 덕분에 Autowired 가능
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    /* @AutoConfigureMockMvc로 대체
    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup( context )
                .apply( SecurityMockMvcConfigurers.springSecurity() )
                .build();
    }*/

    @AfterEach
    void tearDown() throws Exception {
        postRepository.deleteAll();
    }

    // Spring Security 6.2.x > random port Test have a bug, occur IllegalArgumentException
    private MockHttpServletRequest makeRequestMacherServlet( MockHttpServletRequest request ) {
        String matchValue = request.getRequestURI();
        String servlet = "dispatcherServlet";
        String pattern = request.getServletContext().getServletRegistration( servlet ).getMappings().iterator().next();
        HttpServletMapping mapping = new MockHttpServletMapping( matchValue, pattern, servlet, MappingMatch.PATH );
        request.setHttpServletMapping( mapping );
        return request;
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

        /* Security 적용
        ResponseEntity< Long > responseEntity = restTemplate.postForEntity( url, requestSaveDto, Long.class );

        assertEquals( HttpStatus.OK, responseEntity.getStatusCode() );
        assertTrue( responseEntity.getBody() > 0L );

        Long savedId = responseEntity.getBody();*/
        String resString = mvc.perform( MockMvcRequestBuilders
                        .post( url )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( objectMapper.writeValueAsString( requestSaveDto ) )
                        // Security 6.x bug > Servlet 직접 지정
                        .with( request -> makeRequestMacherServlet( request ) )
                )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andReturn()
                .getResponse().getContentAsString( StandardCharsets.UTF_8 );

        Long savedId = Long.valueOf( resString );
        assertTrue( savedId.compareTo( 0L ) > 0 );

        // 단일 조회
        /* Security 적용
        ResponseEntity< PostDto.Select > responseSelectEntity = restTemplate.getForEntity( url + "/" + savedId, PostDto.Select.class );

        assertEquals( HttpStatus.OK, responseSelectEntity.getStatusCode() );
        PostDto.Select selectDto = responseSelectEntity.getBody();*/
        resString = mvc.perform( MockMvcRequestBuilders
                        .get( url + "/" + savedId )
                        // Security 6.x bug > Servlet 직접 지정
                        .with( request -> makeRequestMacherServlet( request ) )
                )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andReturn()
                .getResponse().getContentAsString( StandardCharsets.UTF_8 );

        PostDto.Select selectDto = objectMapper.readValue( resString, PostDto.Select.class );
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

        /* Security 적용
        HttpEntity< PostDto.UpdateRequest > requestUpdateEntity = new HttpEntity<>( requestUpdateDto );
        responseEntity = restTemplate.exchange( url + "/" + savedId, HttpMethod.PUT, requestUpdateEntity, Long.class );

        assertEquals( HttpStatus.OK, responseEntity.getStatusCode() );
        assertTrue( responseEntity.getBody() > 0L );
        assertEquals( savedId, responseEntity.getBody() );*/
        resString = mvc.perform( MockMvcRequestBuilders
                        .put( url + "/" + savedId )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( objectMapper.writeValueAsString( requestUpdateDto ) )
                        // Security 6.x bug > Servlet 직접 지정
                        .with( request -> makeRequestMacherServlet( request ) )
                )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andReturn()
                .getResponse().getContentAsString( StandardCharsets.UTF_8 );

        assertEquals( savedId, Long.valueOf( resString ) );

        // 전체 조회
        List< Post > all = postRepository.findAll();
        assertEquals( 1, all.size() );

        Post selectEntity = all.get( 0 );
        assertEquals( savedId, selectEntity.getId() );
        assertEquals( updateTitle, selectEntity.getTitle() );
        assertEquals( updateContent, selectEntity.getContent() );
        assertEquals( author, selectEntity.getAuthor() );
        System.out.println( "\n" + selectEntity.getCreatedDate() + "\n" + selectEntity.getModifiedDate() );
    }
}