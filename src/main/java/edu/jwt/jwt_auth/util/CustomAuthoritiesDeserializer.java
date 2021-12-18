package edu.jwt.jwt_auth.util;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.*;

public class CustomAuthoritiesDeserializer extends JsonDeserializer<Collection<? extends GrantedAuthority>> {

    @Override
    public Collection<? extends GrantedAuthority> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        if(p.getCurrentToken() == JsonToken.START_ARRAY){

            Set<SimpleGrantedAuthority> modifiedSet = new HashSet<>();

            while(p.nextToken() != JsonToken.END_ARRAY){

                SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(p.getValueAsString());

                modifiedSet.add(simpleGrantedAuthority);
            }

            Set<? extends GrantedAuthority> set = Collections.unmodifiableSet(modifiedSet);

            return set;
        }
        throw ctxt.mappingException("Expected Permissions list");
    }


}
