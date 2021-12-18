package edu.jwt.jwt_auth.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Authority {

    @Id
    private String authority;

    @Override
    public String toString() {
        return authority;
    }
}
