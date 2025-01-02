package org.example.service;

import org.example.entity.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface BankAccountService {

    ResponseEntity<BankAccount> findAccountById(Long id);

    ResponseEntity<List<BankAccount>> accountInfoByClient(UUID id);

    ResponseEntity<?> deleteAccountById(Long id);

    ResponseEntity<User> findUserByAccountId(Long id);

    ResponseEntity<?> createCreditAccount(CreditAccount newAccount);

    ResponseEntity<?> createSavingAccount(SavingAccount newAccount);

    ResponseEntity<?> createTransactionAccount(TransactionAccount newAccount);

    ResponseEntity<?> saveUser(User user);
    ResponseEntity<List<User>> getUsers();

}
