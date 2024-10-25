package org.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bank-account")
public class AccountController {
    @GetMapping("/hello")
    public ResponseEntity<String[]> firstPage() {
        return ResponseEntity.ok(new String[]{"HELLO"});
    }
}
