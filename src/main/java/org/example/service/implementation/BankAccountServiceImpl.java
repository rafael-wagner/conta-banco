package org.example.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.*;
import org.example.repository.AccountRepository;
import org.example.repository.UserRepository;
import org.example.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

    @Autowired
    @Lazy
    private AccountRepository accountRepository;

    @Autowired
    @Lazy
    private UserRepository userRepository;

    @Override
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

    @Override
    public ResponseEntity<List<BankAccount>> accountInfoByClient(UUID id) {
        List<BankAccount> bankAccounts = accountRepository.findByClientId(id);
        return ResponseEntity.ok(bankAccounts);
    }

    @Override
    @Transactional
    public ResponseEntity<?> saveUser(User user) {
        User userSave = new User();
        userSave.setName(user.getName());
        userSave.setEmail(user.getEmail());
        userRepository.save(userSave);
        return ResponseEntity.ok().build();
    }

    @Override
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

    @Override
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

    @Override
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


    @Override
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteAccountById(Long id) {
        Optional<BankAccount> accountDb = accountRepository.findById(id);
        if (accountDb.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        accountRepository.delete(accountDb.get());
        return ResponseEntity.ok().build();
    }

    @Override
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
