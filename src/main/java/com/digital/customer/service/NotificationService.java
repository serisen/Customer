package com.digital.customer.service;

public interface NotificationService {
    void sendEmail(String to, String subject, String htmlContent);
}
