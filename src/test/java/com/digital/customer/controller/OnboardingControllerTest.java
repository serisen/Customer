package com.digital.customer.controller;

import com.digital.customer.MockHelper;
import com.digital.customer.dto.request.OnboardingRequestDto;
import com.digital.customer.kafka.OnboardingProducer;
import com.digital.customer.service.S3StorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OnboardingControllerTest {

    @Mock
    private OnboardingProducer onboardingProducer;

    @Mock
    private S3StorageService s3StorageService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private OnboardingController controller;

    @Test
    void onboardCustomer_successful() throws Exception {
        OnboardingRequestDto requestDto = MockHelper.getOnboardingRequestDto();
        when(bindingResult.hasErrors()).thenReturn(false);

        String mockIdPhotoKey = "mock-id-photo-key";
        String mockPhotoKey = "mock-photo-key";
        when(s3StorageService.upload(any(MultipartFile.class))).thenReturn(mockIdPhotoKey, mockPhotoKey);

        ResponseEntity<String> response = controller.onboardCustomer(requestDto, bindingResult);

        verify(onboardingProducer, times(1)).send(eq(requestDto), eq(mockIdPhotoKey), eq(mockPhotoKey));
        assertEquals(202, response.getStatusCode().value());
        assertEquals("Customer onboarding request is submitted, you will be notified about the onboarding progress!",
                response.getBody());
    }

    @Test
    void validate_whenNoErrors_shouldNotThrow() {
        when(bindingResult.hasErrors()).thenReturn(false);

        assertDoesNotThrow(() -> controller.validate(bindingResult));
    }

    @Test
    void validate_whenErrorsExist_shouldThrowWithCombinedMessage() {
        when(bindingResult.hasErrors()).thenReturn(true);

        ObjectError e1 = new ObjectError("firstName", "First name is required");
        ObjectError e2 = new ObjectError("email", "Email is invalid");

        when(bindingResult.getAllErrors()).thenReturn(List.of(e1, e2));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> controller.validate(bindingResult)
        );

        assertEquals("First name is required, Email is invalid", ex.getMessage());
    }
}
