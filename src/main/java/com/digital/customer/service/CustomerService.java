package com.digital.customer.service;

import com.digital.customer.entity.Customer;
import com.digital.customer.events.OnboardingEvent;

import java.io.IOException;
import java.util.Optional;

public interface CustomerService {
    Customer createCustomer(OnboardingEvent onboardingEvent) throws IOException;
    Optional<Customer> findCustomerBySocialSecurityNumber(String id);
}