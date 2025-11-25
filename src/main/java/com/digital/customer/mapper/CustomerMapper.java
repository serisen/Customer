package com.digital.customer.mapper;

import com.digital.customer.dto.request.OnboardingRequestDto;
import com.digital.customer.entity.Customer;
import com.digital.customer.events.OnboardingEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer toEntity(OnboardingEvent onboardingEvent);
}
