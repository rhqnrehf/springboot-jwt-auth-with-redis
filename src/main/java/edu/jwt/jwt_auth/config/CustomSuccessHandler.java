package edu.jwt.jwt_auth.config;

import edu.jwt.jwt_auth.dto.UserAuthenticationInfo;
import edu.jwt.jwt_auth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;

public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    private final RedisTemplate redisTemplate;

    private final JwtUtil jwtUtil;

    private final long timeout;

    public CustomSuccessHandler(RedisTemplate redisTemplate, JwtUtil jwtUtil, long timeout) {
        this.redisTemplate = redisTemplate;
        this.jwtUtil = jwtUtil;
        this.timeout = timeout;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{
        UserAuthenticationInfo user = (UserAuthenticationInfo) authentication.getPrincipal();

        String jwt = jwtUtil.createToken(user);

        redisTemplate.opsForValue().set(jwt, user, Duration.ofSeconds(timeout));

        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer "+jwt);

        response.sendRedirect("/");
    }
}
