package edu.jwt.jwt_auth.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
public class Member {

    @Id
    private Integer memberIdx;

    private String username;

    private String password;


    @ManyToMany
    @JoinTable(
            name = "member_authorities",
            joinColumns = {@JoinColumn(name="memberIdx",  referencedColumnName = "memberIdx")},
            inverseJoinColumns = {@JoinColumn(name="authority", referencedColumnName = "authority")}
    )
    Set<Authority> authorities = new HashSet<>();

}
