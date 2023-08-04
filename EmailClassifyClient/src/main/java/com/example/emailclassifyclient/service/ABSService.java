package com.example.emailclassifyclient.service;

import org.springframework.web.client.RestTemplate;

public abstract class ABSService {
    protected final RestTemplate rest = new RestTemplate();
}
