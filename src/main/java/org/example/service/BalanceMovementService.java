package org.example.service;

import org.example.dto.BalanceMovementInfoDto;
import org.example.entity.AccountBalanceMovement;
import org.example.entity.BankAccount;
import org.springframework.http.ResponseEntity;

public interface BalanceMovementService {

    ResponseEntity<BankAccount> creditAccountWithdrawal(BalanceMovementInfoDto balanceMovementInfo);

    ResponseEntity<BankAccount> transactionAccountWithdrawal(BalanceMovementInfoDto balanceMovementInfo);

    ResponseEntity<BankAccount> savingAccountWithdrawal(BalanceMovementInfoDto balanceMovementInfo);

    ResponseEntity<BankAccount> creditAccountTransfer(BalanceMovementInfoDto balanceMovementInfo);

    ResponseEntity<BankAccount> transactionAccountTransfer(BalanceMovementInfoDto balanceMovementInfo);

    ResponseEntity<BankAccount> savingAccountTransfer(BalanceMovementInfoDto balanceMovementInfo);

    ResponseEntity<BankAccount> creditAccountDeposit(BalanceMovementInfoDto balanceMovementInfo);

    ResponseEntity<BankAccount> transactionAccountDeposit(BalanceMovementInfoDto balanceMovementInfo);

    ResponseEntity<BankAccount> savingAccountDeposit(BalanceMovementInfoDto balanceMovementInfo);

    BankAccount depositMovement(BankAccount account, AccountBalanceMovement balanceMovement);
}

