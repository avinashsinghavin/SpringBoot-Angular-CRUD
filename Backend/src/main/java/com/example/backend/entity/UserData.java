package com.example.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "userDetails")
public class UserData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    @Column(name="email", unique = true, nullable = false)
    private String email;
    @Column(name="mobile", unique = true, nullable = false)
    private String mobile;
    @Enumerated(EnumType.STRING)
    private State state;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String skills;
    private String photoPath;
}

enum Gender {Male, Female};