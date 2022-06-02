package com.abdiaw.katabank;

import com.abdiaw.katabank.enums.AccountStatus;
import com.abdiaw.katabank.enums.OperationType;
import com.abdiaw.katabank.exceptions.AccountNotFoundException;
import com.abdiaw.katabank.exceptions.BalanceNotSufficientException;
import com.abdiaw.katabank.exceptions.CustomerNotFoundException;
import com.abdiaw.katabank.model.*;
import com.abdiaw.katabank.services.AccountService;
import com.abdiaw.katabank.services.impl.AccountServiceImpl;

import java.util.stream.Stream;

public class Main {


    public static void main(String[] args) throws AccountNotFoundException, BalanceNotSufficientException {

        AccountService accountService = new AccountServiceImpl();

        // Create Customers
        Stream.of("Ababacar", "Mohamed", "Abdoulaye", "Moussa", "Issa", "Hassan", "Yassine", "Aicha").forEach(name -> {
            Customer customer = new Customer();
            customer.setName(name);
            customer.setEmail(name + "@gmail.com");
            accountService.saveCustomer(customer);
        });

        // Creating current Account for customer
        accountService.listCustomers().forEach(customer -> {
            try {
                accountService.createAccount(customer);
            } catch (CustomerNotFoundException e) {
                e.printStackTrace();
            }
        });

        // Create saving Account for some customer
        accountService.getCurrentAccounts().forEach(currentAccount -> {
            try {
                accountService.createSavingAccount(currentAccount);
            } catch (AccountNotFoundException e) {
                e.printStackTrace();
            }
        });

        // Create Operation Type in Account
        accountService.getCurrentAccounts().forEach(currentAccount -> {
            double amount = Math.random() * 12000;
            for (int i = 0; i < 10; i++) {

                    try {
                        if (currentAccount.getBalance() > amount) {
                            accountService.debit(currentAccount.getId(), amount, "DESCRIPTION DEBIT");
                        }
                        else {
                            accountService.credit(currentAccount.getId(), amount, "DESCRIPTION CREDIT");
                        }
                    } catch (AccountNotFoundException | BalanceNotSufficientException e) {
                        e.printStackTrace();
                    }

            }
        });

        // List All Operations in Current Account
        accountService.getCurrentAccounts().forEach(currentAccount -> {
            System.out.println(accountService.accountHistory(currentAccount.getId()));
        });
    }
}
