package edu.jwt.jwt_auth.config;

import edu.jwt.jwt_auth.dto.UserAuthenticationInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@RequiredArgsConstructor
@Configuration
public class RedisConfig {

    private final RedisProperties redisProperties;

    @Bean
    @Primary
    public RedisConnectionFactory redisConnectionFactory(){

        RedisConnectionFactory redisConnectionFactory = new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());

        return redisConnectionFactory;

    }

    @Bean
    @Primary
    public RedisTemplate redisTemplate(){

        RedisTemplate redisTemplate = new RedisTemplate();

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(UserAuthenticationInfo.class));
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        System.out.println(redisTemplate.getConnectionFactory().toString());

        return redisTemplate;
    }

}
