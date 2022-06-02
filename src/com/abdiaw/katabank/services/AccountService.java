package com.abdiaw.katabank.services;

import com.abdiaw.katabank.exceptions.AccountNotFoundException;
import com.abdiaw.katabank.exceptions.BalanceNotSufficientException;
import com.abdiaw.katabank.exceptions.CustomerNotFoundException;
import com.abdiaw.katabank.model.*;

import java.util.List;

public interface AccountService {

    void saveCustomer(Customer customerDTO);

    void createAccount(Customer customerDTO) throws CustomerNotFoundException;

    void createSavingAccount(CurrentAccount currentAccount) throws AccountNotFoundException;

    List<CurrentAccount> getCurrentAccounts();

    List<Customer> listCustomers();

    void debit(String accountId, double amount, String description) throws AccountNotFoundException, BalanceNotSufficientException;

    void credit(String accountId, double amount, String description) throws AccountNotFoundException;

    void transfer(String accountIdSource, String accountIdDestination, double amount) throws AccountNotFoundException, BalanceNotSufficientException;

    List<AccountOperation> accountHistory(String accountId);

}
