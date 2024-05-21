package com.seok.example.book_aws.web.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelloResponseDtoTest {
    @Test
    public void 롬복_정상() {
        String name = "Test";
        int amount = 1000;

        HelloResponseDto dto = new HelloResponseDto( name, amount );

        assertEquals( dto.getName(), name );
        assertEquals( dto.getAmount(), amount );
    }
}