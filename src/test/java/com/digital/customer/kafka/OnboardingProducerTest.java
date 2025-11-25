package com.digital.customer.kafka;

import com.digital.customer.dto.kafka.OnboardingEventToKafka;
import com.digital.customer.dto.request.OnboardingRequestDto;
import com.digital.customer.util.Gender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OnboardingProducerTest {

    private static final String TOPIC = "onboarding-customer-topic";

    @Mock
    KafkaTemplate<String, OnboardingEventToKafka> kafkaTemplate;

    @InjectMocks
    OnboardingProducer producer;

    @Captor
    ArgumentCaptor<OnboardingEventToKafka> requestDtoCaptor;

    @Test
    void testSendWithRequestDto() {
        String idPhotoKey = "id-photo-key";
        String photoKey = "photo-key";
        String socialSecurityNumber = "123-45-6789";
        
        OnboardingRequestDto requestDto = OnboardingRequestDto.builder()
                .firstName("Ser")
                .lastName("Ser")
                .gender(Gender.FEMALE)
                .dateOfBirth(LocalDate.of(1999, 1, 1))
                .phoneNumber("123-456-7890")
                .email("serser@serser.com")
                .nationality("TR")
                .residentialAddress("address")
                .socialSecurityNumber(socialSecurityNumber)
                .idPhoto(mock(MultipartFile.class))
                .photo(mock(MultipartFile.class))
                .build();

        producer.send(requestDto, idPhotoKey, photoKey);

        //verify(kafkaTemplate).send(eq(TOPIC), eq(socialSecurityNumber), eq(requestDtoCaptor.capture()));
    }
}