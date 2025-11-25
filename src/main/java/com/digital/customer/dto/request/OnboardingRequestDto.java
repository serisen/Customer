package com.digital.customer.dto.request;

import com.digital.customer.util.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class OnboardingRequestDto {
    @NotNull(message = "firstName is required")
    private String firstName;

    @NotNull(message = "lastName is required")
    private String lastName;

    @NotNull(message = "gender is required")
    private Gender gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "dateOfBirth is required")
    private LocalDate dateOfBirth;

    @NotNull(message = "phoneNumber is required")
    private String phoneNumber;

    @NotNull(message = "email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "nationality is required")
    private String nationality;

    @NotNull(message = "residentialAddress is required")
    private String residentialAddress;

    @NotNull(message = "socialSecurityNumber is required")
    private String socialSecurityNumber;

    @NotNull(message = "Id Photo is required")
    private MultipartFile idPhoto;
    @NotNull(message = "photo is required")
    private MultipartFile photo;

}

