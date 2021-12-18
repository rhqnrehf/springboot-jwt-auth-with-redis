package edu.jwt.jwt_auth;

import edu.jwt.jwt_auth.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtAuthApplicationTests {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void contextLoads() {

        memberRepository.findWithAuthoritiesByUsername("test");
    }

}
