package com.digital.customer;

import com.digital.customer.dto.request.OnboardingRequestDto;
import com.digital.customer.entity.Account;
import com.digital.customer.entity.Customer;
import com.digital.customer.events.OnboardingEvent;
import com.digital.customer.util.Gender;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;

public class MockHelper {
    public static OnboardingRequestDto getOnboardingRequestDto() {
        MockMultipartFile idPhoto = new MockMultipartFile(
                "idPhoto", "idPhoto.jpg", "image/jpg", "test1".getBytes()
        );

        MockMultipartFile photo = new MockMultipartFile(
                "photo", "photo.jpg", "image/jpg", "test2".getBytes()
        );

        return OnboardingRequestDto.builder()
                .firstName("tst")
                .lastName("tst")
                .email("tst@tst.com")
                .gender(Gender.FEMALE)
                .dateOfBirth(LocalDate.of(1999, 1, 1))
                .socialSecurityNumber("111")
                .idPhoto(idPhoto)
                .photo(photo)
                .build();
    }

    public static OnboardingEvent getOnboardingRequest() {
        return OnboardingEvent.builder()
                .firstName("tst")
                .lastName("tst")
                .email("tst@tst.com")
                .gender(Gender.FEMALE)
                .dateOfBirth(LocalDate.of(1999, 1, 1))
                .socialSecurityNumber("111")
                .idPhotoKey("idPhoto")
                .photoKey("photo")
                .build();
    }

    public static Customer getCustomerEntity() {
        return Customer.builder()
                .customerNumber(1L)
                .email("tst@tst.com")
                .firstName("tst")
                .lastName("tst")
                .build();
    }

    public static Account getAccountEntity() {
        return Account.builder()
                .customerNumber(1L)
                .accountNumber(99L)
                .build();
    }

}
