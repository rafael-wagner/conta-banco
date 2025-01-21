package org.example.service.implementation;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.BalanceMovementInfoDto;
import org.example.entity.*;
import org.example.exception.UnacceptableMovementException;
import org.example.repository.AccountRepository;
import org.example.repository.BalanceMovementRepository;
import org.example.service.BalanceMovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class BalanceMovementServiceImpl implements BalanceMovementService {

    @Autowired
    @Lazy
    private AccountRepository accountRepository;

    @Autowired
    @Lazy
    private BalanceMovementRepository movementRepository;

    private static final Byte MINIMUM_MOVEMENT_VALUE_LIMIT = 1;
    private static final Long MAXIMUM_BALANCE_VALUE = Long.MAX_VALUE;

    @Override
    public ResponseEntity<BankAccount> creditAccountWithdrawal(BalanceMovementInfoDto balanceMovementInfo) {

        BankAccount originCreditAccount =
                this.getBankAccountFromDbByNumber(balanceMovementInfo.getOriginAccountNumber());

        CreditAccountMovement balanceMovement = CreditAccountMovement.builder()
                .movementAccount((CreditAccount) originCreditAccount)
                .transferAccount(null)
                .movementValue(balanceMovementInfo.getValue())
                .movementStartTime(LocalDateTime.now())
                .movementLastStatus(LocalDateTime.now())
                .type(AccountBalanceMovement.MovementType.PHYSICAL_WITHDRAW)
                .status(AccountBalanceMovement.MovementStatus.STARTED)
                .build();
        movementRepository.save(balanceMovement);

        originCreditAccount =
                withdrawFromCreditAccount((CreditAccount) originCreditAccount, balanceMovementInfo.getValue()
                );

        this.saveAccountBalanceUpdate(originCreditAccount, balanceMovement);

        log.debug("retirada de conta realizada com sucesso");

        return ResponseEntity.ok(originCreditAccount);
    }

    private CreditAccount withdrawFromCreditAccount(CreditAccount creditAccount, Long withdrawValue) throws UnacceptableMovementException {

        if (withdrawValue < MINIMUM_MOVEMENT_VALUE_LIMIT) {
            throw new UnacceptableMovementException(UnacceptableMovementException.Reason.BELOW_MINIMUM_VALUE);
        }

        long calcBalance = creditAccount.getBalance() - withdrawValue;
        if (Math.abs(calcBalance) > creditAccount.getCreditLimit()) {
            throw new UnacceptableMovementException(UnacceptableMovementException.Reason.EXCEEDING_CREDIT_LIMIT);
        }
        creditAccount.setBalance(calcBalance);

        return creditAccount;
    }

    @Override
    public ResponseEntity<BankAccount> transactionAccountWithdrawal(BalanceMovementInfoDto movementDto) {

        if (movementDto.getValue() < MINIMUM_MOVEMENT_VALUE_LIMIT) {
            throw new UnacceptableMovementException(UnacceptableMovementException.Reason.BELOW_MINIMUM_VALUE);
        }

        BankAccount originTransactionAccount =
                this.getBankAccountFromDbByNumber(movementDto.getOriginAccountNumber());

        TransactionAccountMovement balanceMovement = TransactionAccountMovement.builder()
                .movementAccount((TransactionAccount) originTransactionAccount)
                .transferAccount(null)
                .movementValue(movementDto.getValue())
                .movementStartTime(LocalDateTime.now())
                .movementLastStatus(LocalDateTime.now())
                .type(AccountBalanceMovement.MovementType.PHYSICAL_WITHDRAW)
                .status(AccountBalanceMovement.MovementStatus.STARTED)
                .build();
        movementRepository.save(balanceMovement);

        originTransactionAccount.setBalance(
                originTransactionAccount.getBalance() - movementDto.getValue()
        );

        this.saveAccountBalanceUpdate(originTransactionAccount, balanceMovement);

        log.debug("retirada de conta realizada com sucesso");

        return ResponseEntity.ok(originTransactionAccount);
    }

    @Override
    public ResponseEntity<BankAccount> savingAccountWithdrawal(BalanceMovementInfoDto movementDto) {

        if (movementDto.getValue() < MINIMUM_MOVEMENT_VALUE_LIMIT) {
            throw new UnacceptableMovementException(UnacceptableMovementException.Reason.BELOW_MINIMUM_VALUE);
        }

        BankAccount originTransactionAccount =
                this.getBankAccountFromDbByNumber(movementDto.getOriginAccountNumber());

        SavingAccountMovement balanceMovement = SavingAccountMovement.builder()
                .movementAccount((SavingAccount) originTransactionAccount)
                .transferAccount(null)
                .movementValue(movementDto.getValue())
                .movementStartTime(LocalDateTime.now())
                .movementLastStatus(LocalDateTime.now())
                .type(movementDto.getMovementType())
                .status(AccountBalanceMovement.MovementStatus.STARTED)
                .build();
        movementRepository.save(balanceMovement);

        originTransactionAccount.setBalance(
                originTransactionAccount.getBalance() - movementDto.getValue()
        );

        this.saveAccountBalanceUpdate(originTransactionAccount, balanceMovement);

        log.debug("retirada de conta realizada com sucesso");

        return ResponseEntity.ok(originTransactionAccount);
    }

    @Override
    @Transactional
    public ResponseEntity<BankAccount> creditAccountTransfer(BalanceMovementInfoDto balanceMovementInfo) {

        this.checkIfValidAccountNumbersForTransfer(balanceMovementInfo);

        BankAccount originAccount =
                this.getBankAccountFromDbByNumber(balanceMovementInfo.getOriginAccountNumber());

        BankAccount destinationAccount =
                this.getBankAccountFromDbByNumber(balanceMovementInfo.getDestinationAccountNumber());

        AccountBalanceMovement balanceMovement = CreditAccountMovement.builder()
                .movementAccount((CreditAccount) originAccount)
                .transferAccount(destinationAccount.getUuid())
                .movementValue(balanceMovementInfo.getValue())
                .movementStartTime(LocalDateTime.now())
                .movementLastStatus(LocalDateTime.now())
                .type(AccountBalanceMovement.MovementType.SENT_TRANSFER)
                .status(AccountBalanceMovement.MovementStatus.STARTED)
                .build();
        movementRepository.save(balanceMovement);

        originAccount = this.withdrawFromCreditAccount((CreditAccount) originAccount, balanceMovementInfo.getValue());
        this.saveAccountBalanceUpdate(originAccount, balanceMovement);

        this.depositTransferAmount(originAccount, destinationAccount, balanceMovementInfo);

        return ResponseEntity.ok(originAccount);
    }

    @Override
    public ResponseEntity<BankAccount> transactionAccountTransfer(BalanceMovementInfoDto balanceMovementInfo) {

        this.checkIfValidAccountNumbersForTransfer(balanceMovementInfo);

        BankAccount originAccount =
                this.getBankAccountFromDbByNumber(balanceMovementInfo.getOriginAccountNumber());

        BankAccount destinationAccount =
                this.getBankAccountFromDbByNumber(balanceMovementInfo.getDestinationAccountNumber());

        AccountBalanceMovement balanceMovement = TransactionAccountMovement.builder()
                .movementAccount((TransactionAccount) originAccount)
                .transferAccount(destinationAccount.getUuid())
                .movementValue(balanceMovementInfo.getValue())
                .movementStartTime(LocalDateTime.now())
                .movementLastStatus(LocalDateTime.now())
                .type(AccountBalanceMovement.MovementType.SENT_TRANSFER)
                .status(AccountBalanceMovement.MovementStatus.STARTED)
                .build();
        movementRepository.save(balanceMovement);

        originAccount = this.withdrawFromTransactionAccount((TransactionAccount) originAccount, balanceMovementInfo.getValue());
        this.saveAccountBalanceUpdate(originAccount, balanceMovement);

        this.depositTransferAmount(originAccount, destinationAccount, balanceMovementInfo);

        return ResponseEntity.ok(originAccount);

    }

    private void checkIfValidAccountNumbersForTransfer(BalanceMovementInfoDto balanceMovementInfo) {
        if (
                balanceMovementInfo.getDestinationAccountNumber() == null ||
                        balanceMovementInfo.getOriginAccountNumber().equals(balanceMovementInfo.getDestinationAccountNumber())
        ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private BankAccount withdrawFromTransactionAccount(TransactionAccount originTransactionAccount, Long value) {

        if (value < MINIMUM_MOVEMENT_VALUE_LIMIT) {
            throw new UnacceptableMovementException(UnacceptableMovementException.Reason.BELOW_MINIMUM_VALUE);
        }

        long withdrawalMaxLimit = originTransactionAccount.getWithdrawalLimit() + originTransactionAccount.getBalance();
        long calcBalance = originTransactionAccount.getBalance() - value;

        if (Math.abs(calcBalance) > withdrawalMaxLimit) {
            throw new UnacceptableMovementException(UnacceptableMovementException.Reason.EXCEEDING_CREDIT_LIMIT);
        }
        originTransactionAccount.setBalance(calcBalance);

        return originTransactionAccount;

    }

    @Override
    public ResponseEntity<BankAccount> savingAccountTransfer(BalanceMovementInfoDto balanceMovementInfo) {

        this.checkIfValidAccountNumbersForTransfer(balanceMovementInfo);

        BankAccount originAccount =
                this.getBankAccountFromDbByNumber(balanceMovementInfo.getOriginAccountNumber());

        BankAccount destinationAccount =
                this.getBankAccountFromDbByNumber(balanceMovementInfo.getDestinationAccountNumber());

        AccountBalanceMovement balanceMovement = SavingAccountMovement.builder()
                .movementAccount((SavingAccount) originAccount)
                .transferAccount(destinationAccount.getUuid())
                .movementValue(balanceMovementInfo.getValue())
                .movementStartTime(LocalDateTime.now())
                .movementLastStatus(LocalDateTime.now())
                .type(AccountBalanceMovement.MovementType.SENT_TRANSFER)
                .status(AccountBalanceMovement.MovementStatus.STARTED)
                .build();
        movementRepository.save(balanceMovement);

        originAccount = this.withdrawFromSavingAccount((SavingAccount) originAccount, balanceMovementInfo.getValue());
        this.saveAccountBalanceUpdate(originAccount, balanceMovement);

        this.depositTransferAmount(originAccount, destinationAccount, balanceMovementInfo);

        return ResponseEntity.ok(originAccount);

    }

    private void depositTransferAmount(
            BankAccount originAccount,
            BankAccount destinationAccount,
            BalanceMovementInfoDto balanceMovementInfo) {

        if (destinationAccount instanceof CreditAccount) {

            AccountBalanceMovement balanceMovementReceivedTransfer = CreditAccountMovement.builder()
                    .movementAccount((CreditAccount) destinationAccount)
                    .transferAccount(originAccount.getUuid())
                    .movementValue(balanceMovementInfo.getValue())
                    .movementStartTime(LocalDateTime.now())
                    .movementLastStatus(LocalDateTime.now())
                    .type(AccountBalanceMovement.MovementType.RECEIVED_TRANSFER)
                    .status(AccountBalanceMovement.MovementStatus.STARTED)
                    .build();

            this.depositMovement(destinationAccount, balanceMovementReceivedTransfer);

        } else if (destinationAccount instanceof TransactionAccount) {

            AccountBalanceMovement balanceMovementReceivedTransfer = TransactionAccountMovement.builder()
                    .movementAccount((TransactionAccount) destinationAccount)
                    .transferAccount(originAccount.getUuid())
                    .movementValue(balanceMovementInfo.getValue())
                    .movementStartTime(LocalDateTime.now())
                    .movementLastStatus(LocalDateTime.now())
                    .type(AccountBalanceMovement.MovementType.RECEIVED_TRANSFER)
                    .status(AccountBalanceMovement.MovementStatus.STARTED)
                    .build();

            this.depositMovement(destinationAccount, balanceMovementReceivedTransfer);

        } else if (destinationAccount instanceof SavingAccount) {

            AccountBalanceMovement balanceMovementReceivedTransfer = SavingAccountMovement.builder()
                    .movementAccount((SavingAccount) destinationAccount)
                    .transferAccount(originAccount.getUuid())
                    .movementValue(balanceMovementInfo.getValue())
                    .movementStartTime(LocalDateTime.now())
                    .movementLastStatus(LocalDateTime.now())
                    .type(AccountBalanceMovement.MovementType.RECEIVED_TRANSFER)
                    .status(AccountBalanceMovement.MovementStatus.STARTED)
                    .build();

            this.depositMovement(destinationAccount, balanceMovementReceivedTransfer);

        } else {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }

    private BankAccount withdrawFromSavingAccount(SavingAccount originSavingAccount, Long value) {

        if (value < MINIMUM_MOVEMENT_VALUE_LIMIT) {
            throw new UnacceptableMovementException(UnacceptableMovementException.Reason.BELOW_MINIMUM_VALUE);
        }

        long withdrawalMaxLimit = originSavingAccount.getBalance();
        long calcBalance = originSavingAccount.getBalance() - value;

        if (Math.abs(calcBalance) > withdrawalMaxLimit) {
            throw new UnacceptableMovementException(UnacceptableMovementException.Reason.EXCEEDING_CREDIT_LIMIT);
        }
        originSavingAccount.setBalance(calcBalance);

        return originSavingAccount;
    }

    @Override
    public ResponseEntity<BankAccount> creditAccountDeposit(BalanceMovementInfoDto balanceMovementInfo) {

        BankAccount destinationAccount =
                this.getBankAccountFromDbByNumber(balanceMovementInfo.getDestinationAccountNumber());

        AccountBalanceMovement balanceMovement = CreditAccountMovement.builder()
                .movementAccount((CreditAccount) destinationAccount)
                .transferAccount(null)
                .movementValue(balanceMovementInfo.getValue())
                .movementStartTime(LocalDateTime.now())
                .movementLastStatus(LocalDateTime.now())
                .type(AccountBalanceMovement.MovementType.MISC)
                .status(AccountBalanceMovement.MovementStatus.STARTED)
                .build();

        destinationAccount = this.depositMovement(destinationAccount, balanceMovement);

        return ResponseEntity.ok(destinationAccount);

    }

    @Override
    public ResponseEntity<BankAccount> transactionAccountDeposit(BalanceMovementInfoDto balanceMovementInfo) {

        BankAccount destinationAccount =
                this.getBankAccountFromDbByNumber(balanceMovementInfo.getDestinationAccountNumber());

        AccountBalanceMovement balanceMovement = TransactionAccountMovement.builder()
                .movementAccount((TransactionAccount) destinationAccount)
                .transferAccount(null)
                .movementValue(balanceMovementInfo.getValue())
                .movementStartTime(LocalDateTime.now())
                .movementLastStatus(LocalDateTime.now())
                .type(AccountBalanceMovement.MovementType.MISC)
                .status(AccountBalanceMovement.MovementStatus.STARTED)
                .build();
        destinationAccount = this.depositMovement(destinationAccount, balanceMovement);

        return ResponseEntity.ok(destinationAccount);
    }

    @Override
    public ResponseEntity<BankAccount> savingAccountDeposit(BalanceMovementInfoDto balanceMovementInfo) {

        BankAccount destinationAccount =
                this.getBankAccountFromDbByNumber(balanceMovementInfo.getDestinationAccountNumber());

        AccountBalanceMovement balanceMovement = SavingAccountMovement.builder()
                .movementAccount((SavingAccount) destinationAccount)
                .transferAccount(null)
                .movementValue(balanceMovementInfo.getValue())
                .movementStartTime(LocalDateTime.now())
                .movementLastStatus(LocalDateTime.now())
                .type(AccountBalanceMovement.MovementType.MISC)
                .status(AccountBalanceMovement.MovementStatus.STARTED)
                .build();
        destinationAccount = this.depositMovement(destinationAccount, balanceMovement);

        return ResponseEntity.ok(destinationAccount);
    }

    @Override
    @Transactional
    public BankAccount depositMovement(BankAccount bankAccount, AccountBalanceMovement balanceMovement) {

        if (balanceMovement.getMovementValue() < MINIMUM_MOVEMENT_VALUE_LIMIT) {
            throw new UnacceptableMovementException(UnacceptableMovementException.Reason.BELOW_MINIMUM_VALUE);
        }

        Long accountFinalBalance = bankAccount.getBalance() + balanceMovement.getMovementValue();
        bankAccount.setBalance(accountFinalBalance);

        this.saveAccountBalanceUpdate(bankAccount, balanceMovement);

        log.debug("deposito realizado com sucesso");
        return bankAccount;
    }

    private BankAccount getBankAccountFromDbByNumber(Long accountNumber) {

        Optional<BankAccount> accountDb = accountRepository.findByAccountNumber(accountNumber);
        if (accountDb.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return accountDb.get();
    }

    @Transactional
    private void saveAccountBalanceUpdate(BankAccount account, AccountBalanceMovement movement) {
        accountRepository.save(account);
        log.debug("conta com valor alterado salvo");
        movement.setStatus(AccountBalanceMovement.MovementStatus.SUCCESS);
        movementRepository.save(movement);
        log.debug("movimentação sucedida salva");
    }

}
