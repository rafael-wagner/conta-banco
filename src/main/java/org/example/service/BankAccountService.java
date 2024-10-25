package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.dao.AccountMovement;
import org.example.dao.UserDao;
import org.example.entity.*;
import org.example.repository.AccountRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class BankAccountService {

    @Autowired
    @Lazy
    private AccountRepository accountRepository;

    @Autowired
    @Lazy
    private UserRepository userRepository;

    public ResponseEntity<BankAccount> findAccountById(Long id) {
        Optional<BankAccount> bankAccountDb = accountRepository.findById(id);
        if (bankAccountDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Optional<User> userDb = userRepository.findByAccount(bankAccountDb.get());
        if(userDb.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        bankAccountDb.get().setUser(userDb.get());
        return ResponseEntity.ok(bankAccountDb.get());

    }

    public ResponseEntity<List<BankAccount>> accountInfoByClient(UUID id) {
        List<BankAccount> bankAccounts = accountRepository.findByClientId(id);
        return ResponseEntity.ok(bankAccounts);
    }

    @Transactional
    public ResponseEntity<BankAccount> accountBalanceMovement(AccountMovement accountMovement) {


        Optional<BankAccount> accountDb = accountRepository.findById(accountMovement.accountId());
        if (accountDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        BankAccount bankAccount = null;
        if (accountMovement.movementType() == AccountMovement.MovementType.WITHDRAWAL) {
            return withdrawalMovement(accountMovement, accountDb.get());
        } else if (accountMovement.movementType() == AccountMovement.MovementType.DEPOSIT) {
            return depositMovement(accountMovement, accountDb.get());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @Transactional
    private ResponseEntity<BankAccount> depositMovement(AccountMovement accountMovement, BankAccount bankAccount) {
        Long accountInitialBalance = bankAccount.getBalance();
        Long accountFinalBalance = accountInitialBalance + accountMovement.value();

        bankAccount.setBalance(accountFinalBalance);
        accountRepository.save(bankAccount);
        return ResponseEntity.ok(bankAccount);
    }


    @Transactional
    private ResponseEntity<BankAccount> withdrawalMovement(AccountMovement accountMovement, BankAccount bankAccount) {

        Long withDrawnAmount = bankAccount.withdraw(accountMovement.value());

        if(withDrawnAmount == 0){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        accountRepository.save(bankAccount);
        return ResponseEntity.ok(bankAccount);
    }

    @Transactional
    public ResponseEntity<?> saveUser(UserDao user) {
        User userSave = new User();
        userSave.setName(user.name());
        userSave.setEmail(user.email());
        userRepository.save(userSave);
        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity<?> createCreditAccount(CreditAccount newAccount) {
        Optional<User> userDb = this.userRepository.findById(newAccount.getUser().getId());
        if(userDb.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        newAccount.setUser(userDb.get());

        this.accountRepository.save(newAccount);
        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity<?> createSavingAccount(SavingAccount newAccount) {
        Optional<User> userDb = this.userRepository.findById(newAccount.getUser().getId());
        if(userDb.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        newAccount.setUser(userDb.get());

        this.accountRepository.save(newAccount);
        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity<?> createTransactionAccount(TransactionAccount newAccount) {
        Optional<User> userDb = this.userRepository.findById(newAccount.getUser().getId());
        if(userDb.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        newAccount.setUser(userDb.get());

        this.accountRepository.save(newAccount);
        return ResponseEntity.ok().build();
    }


    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @Transactional
    public ResponseEntity<?> deleteAccountById(Long id) {
        Optional<BankAccount> accountDb = accountRepository.findById(id);
        if (accountDb.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        accountRepository.delete(accountDb.get());
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<User> findUserByAccountId(Long id) {

        Optional<BankAccount> accountDb = accountRepository.findById(id);
        if(accountDb.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Optional<User> userDb = userRepository.findByAccount(accountDb.get());
        if(userDb.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(userDb.get());
    }
}
