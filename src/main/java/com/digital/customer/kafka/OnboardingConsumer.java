package com.digital.customer.kafka;

import com.digital.customer.dto.kafka.OnboardingEventToKafka;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OnboardingConsumer {

    private final OnboardingProcessor processor;

    @KafkaListener(
            topics = "onboarding-customer-topic",
            groupId = "onboarding-customer-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(OnboardingEventToKafka kafkaDto) {
        log.info("Consuming onboarding request: {}", kafkaDto);
        
        com.digital.customer.events.OnboardingEvent event = com.digital.customer.events.OnboardingEvent.builder()
                .firstName(kafkaDto.getFirstName())
                .lastName(kafkaDto.getLastName())
                .gender(kafkaDto.getGender())
                .dateOfBirth(kafkaDto.getDateOfBirth())
                .phoneNumber(kafkaDto.getPhoneNumber())
                .email(kafkaDto.getEmail())
                .nationality(kafkaDto.getNationality())
                .residentialAddress(kafkaDto.getResidentialAddress())
                .socialSecurityNumber(kafkaDto.getSocialSecurityNumber())
                .idPhotoKey(kafkaDto.getIdPhotoKey())
                .photoKey(kafkaDto.getPhotoKey())
                .build();
        processor.process(event);
    }
}
