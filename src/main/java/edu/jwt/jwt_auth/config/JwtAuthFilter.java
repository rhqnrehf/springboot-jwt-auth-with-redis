package edu.jwt.jwt_auth.config;

import edu.jwt.jwt_auth.dto.UserAuthenticationInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;

@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final RedisTemplate redisTemplate;

    private final long timeout;

    public JwtAuthFilter(RedisTemplate redisTemplate, long timeout){
        this.redisTemplate = redisTemplate;
        this.timeout = timeout;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String jwt = resolveToken(request);

        if(jwt == null){
            log.info("인증정보가 없습니다.");
        } else {

            Object authentication = redisTemplate.opsForValue().getAndExpire(jwt, Duration.ofSeconds(timeout));

            if(authentication == null){
                log.info("만료된 인증정보입니다.");
            } else {
                UserAuthenticationInfo user = (UserAuthenticationInfo) authentication;
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(user, jwt, user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                log.info("인증 성공");
            }
        }

        filterChain.doFilter(request, response);

    }

    private String resolveToken(HttpServletRequest request){

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        String[] authHeaderSplit = authorizationHeader == null ? null : authorizationHeader.split(" ");

        if(authHeaderSplit == null || authHeaderSplit.length < 2){
            return null;
        } else {
            return authHeaderSplit[authHeaderSplit.length-1];
        }

    }

}
