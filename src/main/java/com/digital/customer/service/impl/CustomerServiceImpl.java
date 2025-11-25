package com.digital.customer.service.impl;

import com.digital.customer.events.OnboardingEvent;
import com.digital.customer.entity.Customer;
import com.digital.customer.mapper.CustomerMapper;
import com.digital.customer.repository.CustomerRepository;
import com.digital.customer.service.CustomerService;
import com.digital.customer.service.S3StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final S3StorageService storageService;
    private final CustomerMapper customerMapper;

    @Override
    public Customer createCustomer(OnboardingEvent request) throws IOException {
        log.info("Creating customer from event: {}", request);

        Customer customer = customerMapper.toEntity(request);
        customer.setIdPhotoKey(request.getIdPhotoKey());
        customer.setPhotoKey(request.getPhotoKey());

        return repository.save(customer);
    }

    @Override
    public Optional<Customer> findCustomerBySocialSecurityNumber(String socialSecurityNumber) {
        return repository.findBySocialSecurityNumber(socialSecurityNumber);
    }
}
