package edu.jwt.jwt_auth.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.jwt.jwt_auth.dto.UserAuthenticationInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private final ObjectMapper objectMapper;

    private final String secret;

    private final String header = Base64.getUrlEncoder().withoutPadding()
            .encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes(StandardCharsets.UTF_8));

    public JwtUtil(ObjectMapper objectMapper,@Value("${secret}") String secret) {
        this.objectMapper = objectMapper;
        this.secret = secret;
    }

    public String createToken(UserAuthenticationInfo user) throws JsonProcessingException {

        final Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put("iss", "edu.jwt");
        payloadMap.put("sub", user.getUsername());
        payloadMap.put("iat", System.currentTimeMillis());
        payloadMap.put("authority", user.getAuthorities());

        final String payload = Base64.getUrlEncoder().withoutPadding().encodeToString(objectMapper.writeValueAsString(payloadMap).getBytes(StandardCharsets.UTF_8));

        final StringBuilder jwtBuilder = new StringBuilder();

        jwtBuilder.append(header)
                .append('.')
                .append(payload)
                .append('.');

        try {
            jwtBuilder.append(getSha256(jwtBuilder.substring(0, jwtBuilder.length()-1)));
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }

        return jwtBuilder.toString();
    }


    private String getSha256(String data) throws NoSuchAlgorithmException, InvalidKeyException {

        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));

        byte[] bytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

        return Base64.getUrlEncoder().encodeToString(bytes);

    }

}
