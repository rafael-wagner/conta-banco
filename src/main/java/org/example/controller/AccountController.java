package org.example.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.example.entity.*;
import org.example.entity.jsonView.UserView;
import org.example.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/bank-account/account")
public class AccountController {

    @Autowired
    @Lazy
    private BankAccountService accountService;

    @GetMapping("/{id}")
    @JsonView(UserView.Admin.class)
    public ResponseEntity<BankAccount> accountInfo(
            @PathVariable
            Long id) {

        return accountService.findAccountById(id);
    }

    @PostMapping("/create/credit-account")
    public ResponseEntity<?> createAccount(
            @RequestBody
            @JsonView(UserView.Admin.class)
            CreditAccount newAccount) {
        return accountService.createCreditAccount(newAccount);
    }

    @PostMapping("/create/saving-account")
    public ResponseEntity<?> createAccount(
            @RequestBody
            @JsonView(UserView.Admin.class)
            SavingAccount newAccount) {
        return accountService.createSavingAccount(newAccount);
    }

    @PostMapping("/create/transaction-account")
    public ResponseEntity<?> createAccount(
            @RequestBody
            @JsonView(UserView.Admin.class)
            TransactionAccount newAccount) {
        return accountService.createTransactionAccount(newAccount);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(
            @PathVariable
            Long id) {
        return accountService.deleteAccountById(id);
    }


}
