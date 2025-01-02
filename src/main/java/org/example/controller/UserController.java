package org.example.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.example.entity.BankAccount;
import org.example.entity.User;
import org.example.entity.jsonView.BankAccountView;
import org.example.entity.jsonView.UserView;
import org.example.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bank-account/user")
public class UserController {

    @Autowired
    @Lazy
    private BankAccountService accountService;

    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers(){
        return accountService.getUsers();
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody @JsonView(UserView.Basic.class) User user){
        return accountService.saveUser(user);
    }

    @GetMapping("/{id}")
    @JsonView(BankAccountView.Detailed.class)
    public ResponseEntity<List<BankAccount>> accountInfoByClient(@PathVariable UUID id){
        return accountService.accountInfoByClient(id);
    }

    @GetMapping("/account/{id}")
    @JsonView(UserView.Admin.class)
    public ResponseEntity<User> findUserByAccountId(@PathVariable Long id){
        return accountService.findUserByAccountId(id);
    }

}
