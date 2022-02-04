package com.cokung.comon.service;

public interface EmailService {
    void sendMail(String to, String sub, String text);
}
