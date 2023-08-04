package com.example.emailclassifyclient.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String email;
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<LabelOfUser> labels;
    @JsonIgnore
    @OneToMany(mappedBy = "receiver")
    private List<Email> emails;
}
