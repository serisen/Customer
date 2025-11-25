package com.digital.customer.kafka;

import com.digital.customer.MockHelper;
import com.digital.customer.entity.Account;
import com.digital.customer.entity.Customer;
import com.digital.customer.events.OnboardingEvent;
import com.digital.customer.service.AccountService;
import com.digital.customer.service.CustomerService;
import com.digital.customer.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OnboardingProcessorTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private AccountService accountService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private OnboardingProcessor onboardingProcessor;

    private OnboardingEvent onboardingEvent;
    private Customer customer;
    private Account account;

    @BeforeEach
    void setUp() {
        onboardingEvent = MockHelper.getOnboardingRequest();
        customer = MockHelper.getCustomerEntity();
        account = MockHelper.getAccountEntity();
    }

    @Test
    void process_NewCustomer_Success() throws IOException {
        when(customerService.findCustomerBySocialSecurityNumber(anyString())).thenReturn(Optional.empty());
        when(customerService.createCustomer(any(OnboardingEvent.class))).thenReturn(customer);
        when(accountService.openAccount(anyLong())).thenReturn(account);

        onboardingProcessor.process(onboardingEvent);

        verify(customerService, times(1)).findCustomerBySocialSecurityNumber(onboardingEvent.getSocialSecurityNumber());
        verify(customerService, times(1)).createCustomer(onboardingEvent);
        verify(accountService, times(1)).openAccount(customer.getCustomerNumber());
        verify(notificationService, times(1)).sendEmail(
                eq(onboardingEvent.getEmail()),
                eq(onboardingEvent.getFirstName()),
                any(String.class)
        );
    }

    @Test
    void process_ExistingCustomer_SendsNotificationOnly() throws IOException {
        when(customerService.findCustomerBySocialSecurityNumber(anyString())).thenReturn(Optional.of(customer));
        when(customerService.createCustomer(any(OnboardingEvent.class))).thenReturn(customer);
        when(accountService.openAccount(anyLong())).thenReturn(account);

        onboardingProcessor.process(onboardingEvent);

        verify(customerService, times(1)).findCustomerBySocialSecurityNumber(onboardingEvent.getSocialSecurityNumber());
        verify(customerService, times(1)).createCustomer(onboardingEvent);
        verify(accountService, times(1)).openAccount(customer.getCustomerNumber());
        verify(notificationService, times(2)).sendEmail(
                eq(onboardingEvent.getEmail()),
                eq(onboardingEvent.getFirstName()),
                any(String.class)
        );
    }

    @Test
    void process_ExceptionThrown_LogsError() throws IOException {
        when(customerService.findCustomerBySocialSecurityNumber(anyString())).thenThrow(new RuntimeException("Test exception"));

        onboardingProcessor.process(onboardingEvent);

        verify(customerService, times(1)).findCustomerBySocialSecurityNumber(onboardingEvent.getSocialSecurityNumber());
        verify(customerService, never()).createCustomer(any());
        verify(accountService, never()).openAccount(anyLong());
        verify(notificationService, never()).sendEmail(anyString(), anyString(), anyString());
    }
    
    @Test
    void prepareSuccessfulOnboardingEmail_ReturnsCorrectHtml() throws Exception {
        Method method = OnboardingProcessor.class.getDeclaredMethod("prepareSuccessfulOnboardingEmail", Customer.class, Account.class);
        method.setAccessible(true);
        
        String result = (String) method.invoke(null, customer, account);
        
        assertTrue(result.contains("Dear " + customer.getFirstName()));
        assertTrue(result.contains("You are onboarded to our system"));
        assertTrue(result.contains("You should use your " + account.getCustomerNumber()));
        assertTrue(result.contains("account number " + account.getAccountNumber()));
    }
    
    @Test
    void prepareAlreadyExistingCustomerEmail_ReturnsCorrectHtml() throws Exception {
        Method method = OnboardingProcessor.class.getDeclaredMethod("prepareAlreadyExistingCustomerEmail", Customer.class, Account.class);
        method.setAccessible(true);
        
        String result = (String) method.invoke(null, customer, null);

        assertTrue(result.contains("Dear " + customer.getFirstName()));
        assertTrue(result.contains("You are already in the system"));
        assertTrue(result.contains("Your customer number is " + customer.getCustomerNumber()));
    }
}