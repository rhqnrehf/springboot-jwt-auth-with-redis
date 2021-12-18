package edu.jwt.jwt_auth;

import edu.jwt.jwt_auth.entity.Authority;
import edu.jwt.jwt_auth.entity.Member;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootApplication
public class JwtAuthApplication {
    @PersistenceContext
    EntityManager entityManager;


    public static void main(String[] args) {
        SpringApplication.run(JwtAuthApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void onCreate(){

        Member member = new Member();
        member.setMemberIdx(1);
        member.setUsername("yongjucho");
        member.setPassword("$2a$10$kmsS2shTv4q6aRkCgdvCU.HyD.5zCm4ENORY0oyZ9tjpNkxyE5MHS");

        Authority authority = new Authority();
        authority.setAuthority("ROLE_ADMIN");

        member.getAuthorities().add(authority);

        entityManager.persist(authority);
        entityManager.persist(member);


    }
}
