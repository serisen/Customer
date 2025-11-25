package com.digital.customer.service.impl;

import com.digital.customer.entity.Account;
import com.digital.customer.repository.AccountRepository;
import com.digital.customer.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Override
    public Account openAccount(Long customerNumber) throws IOException {
        Account newAccount = Account.builder()
                .customerNumber(customerNumber)
                .build();

        return accountRepository.save(newAccount);
    }

}
