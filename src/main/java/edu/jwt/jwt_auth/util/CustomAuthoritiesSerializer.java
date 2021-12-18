package edu.jwt.jwt_auth.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import edu.jwt.jwt_auth.dto.UserAuthenticationInfo;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.util.Collection;

public class CustomAuthoritiesSerializer extends JsonSerializer<Collection<? extends GrantedAuthority>> {

    @Override
    public void serialize(Collection<? extends GrantedAuthority> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        gen.writeStartArray();
        for (GrantedAuthority authority : value) {
            gen.writeString(authority.toString());
        }
        gen.writeEndArray();

    }
}
