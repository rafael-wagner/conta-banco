package org.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
public class TransactionAccountMovement extends AccountBalanceMovement {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movement_account_id")
    private TransactionAccount movementAccount;

    @Override
    public BankAccount getBankAccount() {
        return this.movementAccount;
    }
}
