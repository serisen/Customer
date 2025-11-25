package com.digital.customer.service.impl;

import com.digital.customer.service.NotificationService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    @Value("${sendgrid.api-key}")
    private String apiKey;

    private final SendGrid sendGrid;

    @Async
    public void sendEmail(String to, String subject, String htmlContent) {
        Email from = new Email("no-reply@ABC-onboarding.com");
        Email recipient = new Email(to);
        Content content = new Content("text/html", htmlContent);
        Mail mail = new Mail(from, subject, recipient, content);

        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);

            System.out.println("Email sent. Status: " + response.getStatusCode() + ", response" + response.getBody());

        } catch (Exception ex) {
            throw new RuntimeException("Failed to send email", ex);
        }
    }
}
