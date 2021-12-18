package edu.jwt.jwt_auth.config;

import edu.jwt.jwt_auth.dto.UserAuthenticationInfo;
import edu.jwt.jwt_auth.entity.Authority;
import edu.jwt.jwt_auth.repository.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;


@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserAuthenticationInfo user = memberRepository.findWithAuthoritiesByUsername(username)
                .map(member -> {

                    Set<? extends GrantedAuthority> authSet =
                            member.getAuthorities()
                                    .stream()
                                    .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                                    .collect(Collectors.toSet());

                   return new UserAuthenticationInfo(username, member.getPassword(), authSet);
                }).get();

        return user;
    }
}
