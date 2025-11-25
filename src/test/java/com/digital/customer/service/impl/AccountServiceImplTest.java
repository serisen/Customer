package com.digital.customer.service.impl;

import com.digital.customer.MockHelper;
import com.digital.customer.entity.Account;
import com.digital.customer.entity.Customer;
import com.digital.customer.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void openAccount() throws IOException {
        Account mockAccount = MockHelper.getAccountEntity();
        Account accountToSave = Account.builder()
                .customerNumber(1L)
                .build();
        when(accountRepository.save(argThat(a -> a.getCustomerNumber() == 1L)))
                .thenReturn(mockAccount);
        Account savedAccount = accountService.openAccount(accountToSave.getCustomerNumber());
        assertNotNull(savedAccount);
        assertEquals(mockAccount.getCustomerNumber(), savedAccount.getCustomerNumber());
    }
}