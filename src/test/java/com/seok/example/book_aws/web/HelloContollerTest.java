package com.seok.example.book_aws.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith( SpringExtension.class )  // (jUnit4) RunWith( SpringRunner.class )
// Security 적용
@WithMockUser( roles="GUEST" )
@WebMvcTest( controllers=HelloContoller.class )
class HelloContollerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void 정상_동작() throws Exception {
        mvc.perform( get( "/hello" ) )  // 송신 테스트 실행
                .andExpect( status().isOk() )  // 반환 상태 성공인지 확인
                .andExpect( content().string( "hello" ) );  // 반환 내용 문구 확인
    }

    @Test
    void Dto_반환_정상() throws Exception {
        String pName = "홍길동";
        int pAmount = 1000;

        mvc.perform( get( "/hello/dto" )
                        .param( "name", pName )  // 송신 파라미터
                        .param( "amount", String.valueOf( pAmount ) )
                )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.name", is( pName ) ) )  // 반환 json 객체 확인
                .andExpect( jsonPath( "$.amount", is( pAmount ) ) );
    }
}