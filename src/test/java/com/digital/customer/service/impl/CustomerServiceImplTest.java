package com.digital.customer.service.impl;

import com.digital.customer.MockHelper;
import com.digital.customer.entity.Customer;
import com.digital.customer.events.OnboardingEvent;
import com.digital.customer.mapper.CustomerMapper;
import com.digital.customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private OnboardingEvent request;
    private Customer entity;
    private Customer savedEntity;

    @BeforeEach
    void setup() {

        request = MockHelper.getOnboardingRequest();

        entity = MockHelper.getCustomerEntity();

        savedEntity = Customer.builder()
                .customerNumber(1L)
                .idPhotoKey("id-key")
                .photoKey("photo-key")
                .email("tst@tst.com")
                .firstName("tst")
                .lastName("tst")
                .build();
    }

    @Test
    void createCustomer() throws IOException {
        when(customerRepository.save(any())).thenReturn(savedEntity);
        when(customerMapper.toEntity(any(OnboardingEvent.class))).thenReturn(entity);

        Customer response = customerService.createCustomer(request);
        assertNotNull(response);
        assertEquals("tst", response.getFirstName());
        assertEquals(1L, response.getCustomerNumber());

        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void findCustomerBySocialSecurityNumber() {
        when(customerRepository.findBySocialSecurityNumber(any())).thenReturn(Optional.of(savedEntity));
        Optional<Customer> response = customerService.findCustomerBySocialSecurityNumber(request.getSocialSecurityNumber());
        assertNotNull(response);
        assertEquals(1L, response.get().getCustomerNumber());

    }
}