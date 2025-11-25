package com.digital.customer.controller;

import com.digital.customer.dto.request.OnboardingRequestDto;
import com.digital.customer.kafka.OnboardingProducer;
import com.digital.customer.service.S3StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/onboard")
@RequiredArgsConstructor
public class OnboardingController {

    private final OnboardingProducer onboardingProducer;
    private final S3StorageService s3StorageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Onboard customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer onboarded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    public ResponseEntity<String> onboardCustomer(@Valid @ModelAttribute OnboardingRequestDto requestDto, BindingResult bindingResult) throws IOException {
        validate(bindingResult);
        
        try {
            String idPhotoKey = s3StorageService.upload(requestDto.getIdPhoto());
            String photoKey = s3StorageService.upload(requestDto.getPhoto());

            onboardingProducer.send(requestDto, idPhotoKey, photoKey);
            
            return ResponseEntity.accepted().body("Customer onboarding request is submitted, you will be notified about the onboarding progress!");

        } catch (IOException e) {
            log.error("Error uploading files: {}", e.getMessage(), e);
            throw new IOException("Error uploading files: " + e.getMessage(), e);
        }
    }

    static void validate(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            throw new IllegalArgumentException(String.join(", ", errors));
        }
    }
}
