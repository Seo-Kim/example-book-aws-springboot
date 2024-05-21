package com.seok.example.book_aws.web;

import com.seok.example.book_aws.web.dto.HelloResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloContoller {
    @GetMapping( "/hello" )
    public String hello() {
        return "hello";
    }

    @GetMapping( "/hello/dto" )
    public HelloResponseDto helloDto( @RequestParam( "name" ) String name, @RequestParam( "amount" ) Integer amount ) {
        return new HelloResponseDto( name, amount == null ? 0 : amount );
    }
}
