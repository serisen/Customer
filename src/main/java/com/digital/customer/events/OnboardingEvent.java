package com.digital.customer.events;

import com.digital.customer.util.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OnboardingEvent {
    private String email;
    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String nationality;
    private String residentialAddress;
    private String socialSecurityNumber;
    private String idPhotoKey;
    private String photoKey;
}
