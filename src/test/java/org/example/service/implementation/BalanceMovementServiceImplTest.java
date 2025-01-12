package org.example.service.implementation;

import org.example.dto.BalanceMovementInfoDto;
import org.example.entity.*;
import org.example.repository.AccountRepository;
import org.example.repository.BalanceMovementRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BalanceMovementServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private BalanceMovementRepository balanceMovementRepository;

    @InjectMocks
    private BalanceMovementServiceImpl balanceMovementService;

    @Test
    void given_validCreditAccountInfo_whenCreditAccountWithdrawal_then_withdrawalSuccess() {

        BankAccount initialTestAccount = CreditAccount.builder()
                .creditLimit(200000L)
                .taxRate((byte) 0)
                .number(1L)
                .balance(0L)
                .bankOrigin("TestBank")
                .build();

        BalanceMovementInfoDto movementInfoDto = BalanceMovementInfoDto.builder()
                .originAccountNumber(initialTestAccount.getNumber())
                .destinationAccountNumber(null)
                .movementType(AccountBalanceMovement.MovementType.PHYSICAL_WITHDRAW)
                .value(10000L)
                .build();

        final Long expectedEndBalance = initialTestAccount.getBalance() - movementInfoDto.getValue();

        Mockito.when(accountRepository.findByAccountNumber(initialTestAccount.getNumber())).thenReturn(Optional.of(initialTestAccount));

        ResponseEntity<BankAccount> response = balanceMovementService.creditAccountWithdrawal(movementInfoDto);

        final Long endTestBalance = response.getBody().getBalance();
        Assertions.assertEquals(expectedEndBalance, endTestBalance);
    }

    @Test
    void given_validTransactionAccount_when_sufficientFounds_then_successfulWithdrawal() {

        BankAccount initialTestAccount = TransactionAccount.builder()
                .withdrawalLimit(10000L)
                .withdrawalTax((byte) 0)
                .number(1L)
                .balance(0L)
                .bankOrigin("TestBank")
                .build();

        BalanceMovementInfoDto movementInfoDto = BalanceMovementInfoDto.builder()
                .originAccountNumber(initialTestAccount.getNumber())
                .destinationAccountNumber(null)
                .movementType(AccountBalanceMovement.MovementType.PHYSICAL_WITHDRAW)
                .value(10000L)
                .build();

        final Long expectedEndBalance = initialTestAccount.getBalance() - movementInfoDto.getValue();

        Mockito.when(accountRepository.findByAccountNumber(initialTestAccount.getNumber())).thenReturn(Optional.of(initialTestAccount));

        ResponseEntity<BankAccount> response = balanceMovementService.transactionAccountWithdrawal(movementInfoDto);

        final Long endTestBalance = response.getBody().getBalance();
        Assertions.assertEquals(expectedEndBalance, endTestBalance);

    }

    @Test
    void given_validSavingAccount_when_sufficientFounds_then_successfulWithdrawal() {

        BankAccount initialTestAccount = SavingAccount.builder()
                .number(1L)
                .balance(10000L)
                .bankOrigin("TestBank")
                .build();

        BalanceMovementInfoDto movementInfoDto = BalanceMovementInfoDto.builder()
                .originAccountNumber(initialTestAccount.getNumber())
                .destinationAccountNumber(null)
                .movementType(AccountBalanceMovement.MovementType.PHYSICAL_WITHDRAW)
                .value(10000L)
                .build();

        final Long expectedEndBalance = initialTestAccount.getBalance() - movementInfoDto.getValue();

        Mockito.when(accountRepository.findByAccountNumber(initialTestAccount.getNumber())).thenReturn(Optional.of(initialTestAccount));

        ResponseEntity<BankAccount> response = balanceMovementService.savingAccountWithdrawal(movementInfoDto);

        final Long endTestBalance = response.getBody().getBalance();
        Assertions.assertEquals(expectedEndBalance, endTestBalance);

    }

    @Test
    void given_validCreditAccount_when_transferToDifferentCreditAccount_then_SuccessfulTransfer() {

        BankAccount initialTestAccount = CreditAccount.builder()
                .creditLimit(200000L)
                .taxRate((byte) 0)
                .number(1L)
                .balance(0L)
                .bankOrigin("TestBank")
                .build();

        BankAccount transferTestAccount = CreditAccount.builder()
                .creditLimit(200000L)
                .taxRate((byte) 0)
                .number(2L)
                .balance(0L)
                .bankOrigin("TestBank")
                .build();

        BalanceMovementInfoDto movementInfoDto = BalanceMovementInfoDto.builder()
                .originAccountNumber(initialTestAccount.getNumber())
                .destinationAccountNumber(transferTestAccount.getNumber())
                .movementType(AccountBalanceMovement.MovementType.SENT_TRANSFER)
                .value(10000L)
                .build();

        final Long expectedInitialAccountEndBalance = initialTestAccount.getBalance() - movementInfoDto.getValue();
        final Long expectedTransferAccountEndBalance = initialTestAccount.getBalance() + movementInfoDto.getValue();

        Mockito.when(accountRepository.findByAccountNumber(initialTestAccount.getNumber())).thenReturn(Optional.of(initialTestAccount));
        Mockito.when(accountRepository.findByAccountNumber(transferTestAccount.getNumber())).thenReturn(Optional.of(transferTestAccount));

        ResponseEntity<BankAccount> response = balanceMovementService.creditAccountTransfer(movementInfoDto);

        final Long testAccountActualBalance = response.getBody().getBalance();
        final Long transferAccountActualBalance = transferTestAccount.getBalance();
        Assertions.assertEquals(expectedInitialAccountEndBalance, testAccountActualBalance);
        Assertions.assertEquals(expectedTransferAccountEndBalance,transferAccountActualBalance);

    }

    @Test
    void given_validTransactionAccount_when_transferToDifferentTransactionAccount_then_SuccessfulTransfer() {

        BankAccount initialTestAccount = TransactionAccount.builder()
                .withdrawalLimit(10000L)
                .withdrawalTax((byte) 0)
                .number(1L)
                .balance(0L)
                .bankOrigin("TestBank")
                .build();

        BankAccount transferTestAccount = TransactionAccount.builder()
                .withdrawalLimit(10000L)
                .withdrawalTax((byte) 0)
                .number(2L)
                .balance(0L)
                .bankOrigin("TestBank")
                .build();

        BalanceMovementInfoDto movementInfoDto = BalanceMovementInfoDto.builder()
                .originAccountNumber(initialTestAccount.getNumber())
                .destinationAccountNumber(transferTestAccount.getNumber())
                .movementType(AccountBalanceMovement.MovementType.SENT_TRANSFER)
                .value(10000L)
                .build();

        final Long expectedInitialAccountEndBalance = initialTestAccount.getBalance() - movementInfoDto.getValue();
        final Long expectedTransferAccountEndBalance = initialTestAccount.getBalance() + movementInfoDto.getValue();

        Mockito.when(accountRepository.findByAccountNumber(initialTestAccount.getNumber())).thenReturn(Optional.of(initialTestAccount));
        Mockito.when(accountRepository.findByAccountNumber(transferTestAccount.getNumber())).thenReturn(Optional.of(transferTestAccount));

        ResponseEntity<BankAccount> response = balanceMovementService.transactionAccountTransfer(movementInfoDto);

        final Long testAccountActualBalance = response.getBody().getBalance();
        final Long transferAccountActualBalance = transferTestAccount.getBalance();
        Assertions.assertEquals(expectedInitialAccountEndBalance, testAccountActualBalance);
        Assertions.assertEquals(expectedTransferAccountEndBalance,transferAccountActualBalance);

    }

    @Test
    void given_validSavingAccount_when_validTransferToOtherSavingAccount_then_successfulDeposit() {

        BankAccount initialTestAccount = SavingAccount.builder()
                .number(1L)
                .balance(10000L)
                .bankOrigin("TestBank")
                .build();

        BankAccount transferTestAccount = TransactionAccount.builder()
                .number(2L)
                .balance(10000L)
                .bankOrigin("TestBank")
                .build();

        BalanceMovementInfoDto movementInfoDto = BalanceMovementInfoDto.builder()
                .originAccountNumber(initialTestAccount.getNumber())
                .destinationAccountNumber(transferTestAccount.getNumber())
                .movementType(AccountBalanceMovement.MovementType.SENT_TRANSFER)
                .value(10000L)
                .build();

        final Long expectedInitialAccountEndBalance = initialTestAccount.getBalance() - movementInfoDto.getValue();
        final Long expectedTransferAccountEndBalance = initialTestAccount.getBalance() + movementInfoDto.getValue();

        Mockito.when(accountRepository.findByAccountNumber(initialTestAccount.getNumber())).thenReturn(Optional.of(initialTestAccount));
        Mockito.when(accountRepository.findByAccountNumber(transferTestAccount.getNumber())).thenReturn(Optional.of(transferTestAccount));

        ResponseEntity<BankAccount> response = balanceMovementService.savingAccountTransfer(movementInfoDto);

        final Long testAccountActualBalance = response.getBody().getBalance();
        final Long transferAccountActualBalance = transferTestAccount.getBalance();
        Assertions.assertEquals(expectedInitialAccountEndBalance, testAccountActualBalance);
        Assertions.assertEquals(expectedTransferAccountEndBalance,transferAccountActualBalance);
    }

    @Test
    void given_validCreditAccount_when_validDeposit_then_successfulDeposit() {

        BankAccount initialTestAccount = CreditAccount.builder()
                .creditLimit(200000L)
                .taxRate((byte) 0)
                .number(1L)
                .balance(0L)
                .bankOrigin("TestBank")
                .build();

        BalanceMovementInfoDto movementInfoDto = BalanceMovementInfoDto.builder()
                .originAccountNumber(null)
                .destinationAccountNumber(initialTestAccount.getNumber())
                .movementType(AccountBalanceMovement.MovementType.MISC)
                .value(10000L)
                .build();

        final long expectedInitialTestAccountFinalBalance = initialTestAccount.getBalance() + movementInfoDto.getValue();
        Mockito.when(accountRepository.findByAccountNumber(initialTestAccount.getNumber())).thenReturn(Optional.of(initialTestAccount));
        balanceMovementService.creditAccountDeposit(movementInfoDto);
        final long actualBalance = initialTestAccount.getBalance();

        Assertions.assertEquals(expectedInitialTestAccountFinalBalance,actualBalance);

    }

    @Test
    void given_validTransactionAccount_when_validDeposit_then_successfulDeposit() {

        BankAccount initialTestAccount = TransactionAccount.builder()
                .withdrawalLimit(10000L)
                .withdrawalTax((byte) 0)
                .number(1L)
                .balance(0L)
                .bankOrigin("TestBank")
                .build();

        BalanceMovementInfoDto movementInfoDto = BalanceMovementInfoDto.builder()
                .originAccountNumber(null)
                .destinationAccountNumber(initialTestAccount.getNumber())
                .movementType(AccountBalanceMovement.MovementType.MISC)
                .value(10000L)
                .build();

        final long expectedInitialTestAccountFinalBalance = initialTestAccount.getBalance() + movementInfoDto.getValue();
        Mockito.when(accountRepository.findByAccountNumber(initialTestAccount.getNumber())).thenReturn(Optional.of(initialTestAccount));
        balanceMovementService.transactionAccountDeposit(movementInfoDto);
        final long actualBalance = initialTestAccount.getBalance();

        Assertions.assertEquals(expectedInitialTestAccountFinalBalance,actualBalance);

    }

    @Test
    void given_validSavingAccount_when_validDeposit_then_successfulDeposit() {

        BankAccount initialTestAccount = SavingAccount.builder()
                .number(1L)
                .balance(10000L)
                .bankOrigin("TestBank")
                .build();

        BalanceMovementInfoDto movementInfoDto = BalanceMovementInfoDto.builder()
                .originAccountNumber(null)
                .destinationAccountNumber(initialTestAccount.getNumber())
                .movementType(AccountBalanceMovement.MovementType.MISC)
                .value(10000L)
                .build();

        final long expectedInitialTestAccountFinalBalance = initialTestAccount.getBalance() + movementInfoDto.getValue();
        Mockito.when(accountRepository.findByAccountNumber(initialTestAccount.getNumber())).thenReturn(Optional.of(initialTestAccount));
        balanceMovementService.savingAccountDeposit(movementInfoDto);
        final long actualBalance = initialTestAccount.getBalance();

        Assertions.assertEquals(expectedInitialTestAccountFinalBalance,actualBalance);

    }

}