package edu.jwt.jwt_auth.repository;

import edu.jwt.jwt_auth.entity.Authority;
import edu.jwt.jwt_auth.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
@Transactional
public interface MemberRepository extends JpaRepository<Member, Integer> {


    @EntityGraph(attributePaths = "authorities")
    abstract Optional<Member> findWithAuthoritiesByUsername(String username);
}
