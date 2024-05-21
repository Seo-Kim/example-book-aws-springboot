package com.seok.example.book_aws.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith( SpringExtension.class )
@WebMvcTest( controllers=HelloContoller.class )
class HelloContollerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void 정상_동작() throws Exception {
        mvc.perform( get( "/hello" ) )
                .andExpect( status().isOk() )
                .andExpect( content().string( "hello" ) );
    }

    @Test
    void Dto_반환_정상() throws Exception {
        String pName = "홍길동";
        int pAmount = 1000;

        mvc.perform( get( "/hello/dto" )
                        .param( "name", pName )
                        .param( "amount", String.valueOf( pAmount ) )
                )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.name", is( pName ) ) )
                .andExpect( jsonPath( "$.amount", is( pAmount ) ) );
    }
}