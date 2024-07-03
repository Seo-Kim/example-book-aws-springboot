package com.seok.example.book_aws.web;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProfileController {
    private final Environment env;
      // 생성자 주입의 장점: Test 등 외부에서 활용할 때 MockEnvironment 등 구현체를 바로 활용 가능 (Spring 실행을 강제하지 않음)

    @GetMapping( "/profile" )
    public String profile() {
        // Active Profile 모두 가져옴
        List< String > profiles = Arrays.asList( env.getActiveProfiles() );
        List< String > findProfiles = Arrays.asList( "real1", "real2", "real" );

        return profiles.stream()
                // 찾고자 하는 Profile만 검색
                .filter( findProfiles::contains )
                .findAny()
                // 검색된 것이 없는 경우
                .orElse( profiles.isEmpty() ? "default" : profiles.get( 0 ) );
    }
}