package edu.jwt.jwt_auth.config;

import edu.jwt.jwt_auth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final RedisTemplate redisTemplate;
    private final JwtUtil jwtUtil;
    private final long timeout;

    public SecurityConfig(RedisTemplate redisTemplate, JwtUtil jwtUtil, @Value("${timeout}") long timeout) {
        this.redisTemplate = redisTemplate;
        this.jwtUtil = jwtUtil;
        this.timeout = timeout;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest()
                .authenticated()

                .and()
                /**
                 * 세션을 사용하지않으니 STATELESS 설정
                 */
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin()
                .successHandler(new CustomSuccessHandler(redisTemplate, jwtUtil, timeout))
                .and()
                .addFilterAfter(new JwtAuthFilter(redisTemplate, timeout), SecurityContextPersistenceFilter.class);


    }

    @Override
    public void configure(WebSecurity web) throws Exception {

        web.ignoring()
                .antMatchers("/h2/**")
                .and()
                .debug(true);

    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.authenticationProvider(new TestProvider());
        super.configure(auth);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
