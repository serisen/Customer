package com.digital.customer.kafka;

import com.digital.customer.dto.kafka.OnboardingEventToKafka;
import com.digital.customer.events.OnboardingEvent;
import com.digital.customer.util.Gender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OnboardingConsumerTest {

    @Mock
    OnboardingProcessor onboardingProcessor;

    @InjectMocks
    OnboardingConsumer consumer;
    
    @Captor
    ArgumentCaptor<OnboardingEvent> eventCaptor;

    @Test
    void consume() {
        // Create test data
        String firstName = "John";
        String lastName = "Doe";
        Gender gender = Gender.MALE;
        LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);
        String phoneNumber = "1234567890";
        String email = "john.doe@example.com";
        String nationality = "US";
        String residentialAddress = "123 Main St";
        String socialSecurityNumber = "123-45-6789";
        String idPhotoKey = "id-photo-key";
        String photoKey = "photo-key";
        
        // Create the Kafka DTO with test data
        OnboardingEventToKafka requestDto = OnboardingEventToKafka.builder()
                .firstName(firstName)
                .lastName(lastName)
                .gender(gender)
                .dateOfBirth(dateOfBirth)
                .phoneNumber(phoneNumber)
                .email(email)
                .nationality(nationality)
                .residentialAddress(residentialAddress)
                .socialSecurityNumber(socialSecurityNumber)
                .idPhotoKey(idPhotoKey)
                .photoKey(photoKey)
                .build();
        
        // Call the method under test
        consumer.consume(requestDto);
        
        // Verify that processor.process() was called with the correct OnboardingEvent
        verify(onboardingProcessor).process(eventCaptor.capture());
        
        // Get the captured OnboardingEvent
        OnboardingEvent capturedEvent = eventCaptor.getValue();
        
        // Verify that all fields were correctly mapped
        assertEquals(firstName, capturedEvent.getFirstName());
        assertEquals(lastName, capturedEvent.getLastName());
        assertEquals(gender, capturedEvent.getGender());
        assertEquals(dateOfBirth, capturedEvent.getDateOfBirth());
        assertEquals(phoneNumber, capturedEvent.getPhoneNumber());
        assertEquals(email, capturedEvent.getEmail());
        assertEquals(nationality, capturedEvent.getNationality());
        assertEquals(residentialAddress, capturedEvent.getResidentialAddress());
        assertEquals(socialSecurityNumber, capturedEvent.getSocialSecurityNumber());
        assertEquals(idPhotoKey, capturedEvent.getIdPhotoKey());
        assertEquals(photoKey, capturedEvent.getPhotoKey());
    }
}