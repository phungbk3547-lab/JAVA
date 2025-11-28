package com.example.service.impl;

import com.example.service.EmailService;
import org.springframework.stereotype.Service;

@Service
public class FakeEmailService implements EmailService {

    @Override
    public void send(String to, String subject, String content) {
        System.out.println("===== FAKE EMAIL SERVICE =====");
        System.out.println("TO: " + to);
        System.out.println("SUBJECT: " + subject);
        System.out.println("CONTENT:\n" + content);
        System.out.println("==============================");
    }
}
