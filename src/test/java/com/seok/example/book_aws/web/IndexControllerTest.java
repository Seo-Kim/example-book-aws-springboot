package com.seok.example.book_aws.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith( SpringExtension.class )
@SpringBootTest( webEnvironment=RANDOM_PORT )
class IndexControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void 메인_로딩() {
        String body = restTemplate.getForObject( "/", String.class );

        assertNotNull( body );
        /*System.out.println( Charset.defaultCharset().displayName() );
        System.out.println( body );*/
        assertTrue( body.contains( "스프링 부트로 시작하는 웹 서비스" ) );
    }
}