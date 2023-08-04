package com.example.emailclassifyclient.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition = "TEXT")
    private String subject;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String content;
    private String sentTime;
    private String sender;
    @ManyToOne
    private User receiver;
    @ManyToOne
    private Label label;
}
