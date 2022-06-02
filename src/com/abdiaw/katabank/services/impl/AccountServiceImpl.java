package com.abdiaw.katabank.services.impl;

import com.abdiaw.katabank.enums.AccountStatus;
import com.abdiaw.katabank.enums.OperationType;
import com.abdiaw.katabank.exceptions.AccountNotFoundException;
import com.abdiaw.katabank.exceptions.BalanceNotSufficientException;
import com.abdiaw.katabank.exceptions.CustomerNotFoundException;
import com.abdiaw.katabank.model.*;
import com.abdiaw.katabank.services.AccountService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class AccountServiceImpl implements AccountService {

    private final List<Customer> customers;
    private final List<Account> accounts;
    private final List<AccountOperation> accountOperations;
    private final List<CurrentAccount> currentAccounts;

    public AccountServiceImpl() {
        this.customers = new ArrayList<>();
        this.accounts = new ArrayList<>();
        this.accountOperations = new ArrayList<>();
        this.currentAccounts = new ArrayList<>();
    }

    @Override
    public void saveCustomer(Customer customerDTO) {
        System.out.println("Saving new Customer");
        Customer customer = new Customer();
        customer.setEmail(customerDTO.getEmail());
        customer.setName(customerDTO.getName());
        customer.setId(UUID.randomUUID().toString());
        this.customers.add(customer);
    }

    @Override
    public void createAccount(Customer customer) throws CustomerNotFoundException {
        if (searchCustomerById(customer.getId()) == null)
            throw new CustomerNotFoundException("Customer not found");
        System.out.println("Crating new Account for customer : " + customer.getName());
        CurrentAccount account = new CurrentAccount();
        account.setId(UUID.randomUUID().toString());
        account.setBalance(Math.random() * 90000);
        account.setCreatedAt(new Date());
        account.setStatus(AccountStatus.CREATED);
        account.setCustomer(customer);
        account.setOverDraft(9000);
        this.currentAccounts.add(account);
        this.accounts.add(account);
    }

    @Override
    public void createSavingAccount(CurrentAccount currentAccount) throws AccountNotFoundException {
        if (currentAccount == null)
            throw new AccountNotFoundException("Account not found");
        System.out.println("Crating new Saving Account for customer : " + currentAccount.getCustomer().getName());
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setInterestRate(5.5);
        savingAccount.setId(currentAccount.getId());
        savingAccount.setBalance(currentAccount.getBalance());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setStatus(AccountStatus.ACTIVATED);
        savingAccount.setCustomer(currentAccount.getCustomer());
        this.accounts.add(savingAccount);
    }

    @Override
    public List<CurrentAccount> getCurrentAccounts() {
        return this.currentAccounts;
    }

    @Override
    public List<Customer> listCustomers() {
        return this.customers;
    }

    @Override
    public void debit(String accountId, double amount, String description) throws AccountNotFoundException, BalanceNotSufficientException {
        Account bankAccount = searchAccountById(accountId);
        if (bankAccount == null)
            throw new AccountNotFoundException("BankAccount not found");
        if (bankAccount.getBalance() < amount)
            throw new BalanceNotSufficientException("Balance not sufficient");
        System.out.println("DEBIT : " + amount + ", ACCOUNT ID : " + accountId);
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        this.accountOperations.add(accountOperation);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws AccountNotFoundException {
        Account bankAccount = searchAccountById(accountId);
        if (bankAccount == null)
            throw new AccountNotFoundException("BankAccount not found");
        System.out.println("CREDIT : " + amount + ", ACCOUNT ID : " + accountId);
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setId(accountId);
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        this.accountOperations.add(accountOperation);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws AccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource, amount, "Transfer to " + accountIdDestination);
        credit(accountIdDestination, amount, "Transfer from " + accountIdSource);
    }

    @Override
    public List<AccountOperation> accountHistory(String accountId) {
        List<AccountOperation> accountOperations = new ArrayList<>();
        this.accountOperations.forEach(accountOperation -> {
            if (accountOperation.getBankAccount().getId().equals(accountId)) {
                accountOperations.add(accountOperation);
            }
        });
        return accountOperations;
    }

    public Customer searchCustomerById(String id) {
        for (Customer customer1 : this.customers) {
            if (customer1.getId().equals(id)) {
                return customer1;
            }
        }
        return null;
    }

    public Account searchAccountById(String id) {
        for (Account account : this.accounts) {
            if (account.getId().equals(id)) {
                return account;
            }
        }
        return null;
    }
}
