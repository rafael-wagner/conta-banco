package org.example.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.example.dto.BalanceMovementInfoDto;
import org.example.entity.BankAccount;
import org.example.entity.jsonView.BalanceMovementView;
import org.example.service.BalanceMovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bank-account/balance-movement")
public class BalanceMovementController {

    @Autowired
    @Lazy
    BalanceMovementService balanceMovementService;


    @PutMapping("/withdrawal/credit-account")
    @JsonView(BalanceMovementView.Detailed.class)
    public ResponseEntity<BankAccount> creditAccountWithdrawal(
            @RequestBody
            BalanceMovementInfoDto balanceMovementInfo
    ) {
        return balanceMovementService.creditAccountWithdrawal(balanceMovementInfo);
    }

    @PutMapping("/withdrawal/transfer-account")
    @JsonView(BalanceMovementView.Detailed.class)
    public ResponseEntity<BankAccount> transferAccountWithdrawal(
            @RequestBody
            BalanceMovementInfoDto balanceMovementInfo
    ) {
        return balanceMovementService.transactionAccountWithdrawal(balanceMovementInfo);
    }

    @PutMapping("/withdrawal/saving-account")
    @JsonView(BalanceMovementView.Detailed.class)
    public ResponseEntity<BankAccount> savingAccountWithdrawal(
            @RequestBody
            BalanceMovementInfoDto balanceMovementInfo
    ) {
        return balanceMovementService.savingAccountWithdrawal(balanceMovementInfo);
    }

    @PutMapping("/transfer/credit-account")
    @JsonView(BalanceMovementView.Detailed.class)
    public ResponseEntity<BankAccount> creditAccountTransfer(
            @RequestBody
            BalanceMovementInfoDto balanceMovementInfo
    ) {
        return balanceMovementService.creditAccountTransfer(balanceMovementInfo);
    }

    @PutMapping("/transfer/transfer-account")
    @JsonView(BalanceMovementView.Detailed.class)
    public ResponseEntity<BankAccount> transferAccountTransfer(
            @RequestBody
            BalanceMovementInfoDto balanceMovementInfo
    ) {
        return balanceMovementService.transactionAccountTransfer(balanceMovementInfo);
    }

    @PutMapping("/transfer/saving-account")
    @JsonView(BalanceMovementView.Detailed.class)
    public ResponseEntity<BankAccount> savingAccountTransfer(
            @RequestBody
            BalanceMovementInfoDto balanceMovementInfo
    ) {
        return balanceMovementService.savingAccountTransfer(balanceMovementInfo);
    }

    @PutMapping("/deposit/credit-account")
    @JsonView(BalanceMovementView.Detailed.class)
    public ResponseEntity<BankAccount> creditAccountDepositMovement(
            @RequestBody
            BalanceMovementInfoDto balanceMovementInfo) {
        return balanceMovementService.creditAccountDeposit(balanceMovementInfo);
    }

    @PutMapping("/deposit/transfer-account")
    @JsonView(BalanceMovementView.Detailed.class)
    public ResponseEntity<BankAccount> transferAccountDepositMovement(
            @RequestBody
            BalanceMovementInfoDto balanceMovementInfo) {
        return balanceMovementService.transactionAccountDeposit(balanceMovementInfo);
    }

    @PutMapping("/deposit/saving-account")
    @JsonView(BalanceMovementView.Detailed.class)
    public ResponseEntity<BankAccount> savingAccountDepositMovement(
            @RequestBody
            BalanceMovementInfoDto balanceMovementInfo) {
        return balanceMovementService.savingAccountDeposit(balanceMovementInfo);
    }

}
