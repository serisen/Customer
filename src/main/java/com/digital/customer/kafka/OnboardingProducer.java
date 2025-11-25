package com.digital.customer.kafka;

import com.digital.customer.dto.kafka.OnboardingEventToKafka;
import com.digital.customer.dto.request.OnboardingRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OnboardingProducer {

    private final KafkaTemplate<String, OnboardingEventToKafka> kafkaTemplate;
    private static final String TOPIC = "onboarding-customer-topic";

    public void send(OnboardingRequestDto requestDto, String idPhotoKey, String photoKey) {
        OnboardingEventToKafka kafkaDto = OnboardingEventToKafka.builder()
                .firstName(requestDto.getFirstName())
                .lastName(requestDto.getLastName())
                .gender(requestDto.getGender())
                .dateOfBirth(requestDto.getDateOfBirth())
                .phoneNumber(requestDto.getPhoneNumber())
                .email(requestDto.getEmail())
                .nationality(requestDto.getNationality())
                .residentialAddress(requestDto.getResidentialAddress())
                .socialSecurityNumber(requestDto.getSocialSecurityNumber())
                .idPhotoKey(idPhotoKey)
                .photoKey(photoKey)
                .build();
        
        log.info("Sending onboarding request to Kafka: {}", kafkaDto);
        kafkaTemplate.send(TOPIC, String.valueOf(kafkaDto.getSocialSecurityNumber()), kafkaDto);
        log.info("Sent onboarding request to Kafka: {}", kafkaDto);
    }

}
