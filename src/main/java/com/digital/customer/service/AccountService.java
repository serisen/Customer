package com.digital.customer.service;

import com.digital.customer.entity.Account;

import java.io.IOException;

public interface AccountService {
    Account openAccount(Long customerNumber) throws IOException;

}
