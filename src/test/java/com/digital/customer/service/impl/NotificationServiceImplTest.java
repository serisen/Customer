package com.digital.customer.service.impl;

import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private SendGrid sendGrid;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    void sendEmail_successful() throws IOException {
        Response mockResponse = new Response();
        mockResponse.setStatusCode(202);
        mockResponse.setBody("Accepted");

        when(sendGrid.api(any(Request.class))).thenReturn(mockResponse);

        notificationService.sendEmail("test@example.com", "Subject", "<h1>Hello</h1>");

        verify(sendGrid, times(1)).api(any(Request.class));
    }

    @Test
    void sendEmail_failure_shouldThrow() throws IOException {
        when(sendGrid.api(any(Request.class))).thenThrow(new IOException("SendGrid error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                notificationService.sendEmail("test@test.com", "Subject", "<h1>Onboarded</h1>"));

        assertTrue(exception.getMessage().contains("Failed to send email"));
        verify(sendGrid, times(1)).api(any(Request.class));
    }

}