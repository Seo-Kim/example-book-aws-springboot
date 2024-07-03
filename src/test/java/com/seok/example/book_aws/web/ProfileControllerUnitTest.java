package com.seok.example.book_aws.web;

import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProfileControllerUnitTest {
    @Test
    public void 조회_정상() {
        String trgProfile = "real";

        MockEnvironment env = new MockEnvironment();
        env.setActiveProfiles( trgProfile, "oauth", "db-real" );

        ProfileController controller = new ProfileController( env );
        String findProfile = controller.profile();

        assertEquals( trgProfile, findProfile );
    }

    @Test
    public void 조회_첫째() {
        MockEnvironment env = new MockEnvironment();
        env.setActiveProfiles( "oauth", "db-real" );

        ProfileController controller = new ProfileController( env );
        String findProfile = controller.profile();

        assertEquals( "oauth", findProfile );
    }

    @Test
    public void 조회_default() {
        MockEnvironment env = new MockEnvironment();

        ProfileController controller = new ProfileController( env );
        String findProfile = controller.profile();

        assertEquals( "default", findProfile );
    }
}