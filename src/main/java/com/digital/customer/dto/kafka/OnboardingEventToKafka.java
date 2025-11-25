package com.digital.customer.dto.kafka;

import com.digital.customer.util.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnboardingEventToKafka implements Serializable {
    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String email;
    private String nationality;
    private String residentialAddress;
    private String socialSecurityNumber;
    private String idPhotoKey;
    private String photoKey;
}