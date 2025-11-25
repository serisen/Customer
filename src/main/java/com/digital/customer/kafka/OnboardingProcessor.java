package com.digital.customer.kafka;

import com.digital.customer.entity.Account;
import com.digital.customer.entity.Customer;
import com.digital.customer.events.OnboardingEvent;
import com.digital.customer.service.AccountService;
import com.digital.customer.service.CustomerService;
import com.digital.customer.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OnboardingProcessor {

    private final CustomerService customerService;
    private final AccountService accountService;
    private final NotificationService notificationService;

    public void process(OnboardingEvent request) {
        String emailBodyToSend ;
        try {
            log.info("Onboarding customer: {}", request);
            Optional<Customer> existingCustomer = customerService.findCustomerBySocialSecurityNumber(request.getSocialSecurityNumber());
            if(existingCustomer.isPresent()) {
                emailBodyToSend = prepareAlreadyExistingCustomerEmail(existingCustomer.get(), null);
                notificationService.sendEmail(request.getEmail(), request.getFirstName(), emailBodyToSend);
            }
            Customer customer = customerService.createCustomer(request);
            Account savedAccount = accountService.openAccount(customer.getCustomerNumber());

            emailBodyToSend = prepareSuccessfulOnboardingEmail(customer, savedAccount);
            notificationService.sendEmail(request.getEmail(), request.getFirstName(), emailBodyToSend);

            log.info("Customer successfully onboarded.");
        } catch (Exception ex) {
            log.error("Onboarding failed: {}", ex.getMessage(), ex);
            // TODO failed event handle
        }
    }

    private static String prepareSuccessfulOnboardingEmail(Customer savedCustomer, Account savedAccount) {
        String html = "<h1>Dear " + savedCustomer.getFirstName() + ",</h1>" +
                "<p>You are onboarded to our system. Your account has been successfully created.</p>"
                +"<p> You should use your " + savedAccount.getCustomerNumber() + " and account number " + savedAccount.getAccountNumber() +"</p>";
        return html;
    }
    private static String prepareAlreadyExistingCustomerEmail(Customer savedCustomer, Account savedAccount) {
        String html = "<h1>Dear " + savedCustomer.getFirstName() + ",</h1>" +
                "<p>You are already in the system!</p>"
                +"<p> Your customer number is " + savedCustomer.getCustomerNumber() + "</p>";
        return html;
    }

}