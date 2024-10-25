package org.example.entity;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.entity.jsonView.BankAccountView;

import java.io.Serializable;
import java.util.Objects;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
public class SavingAccount extends BankAccount implements Serializable {

    @JsonView(BankAccountView.Detailed.class)
    private Byte interestRate;

    public SavingAccount() {
    }

    public SavingAccount(Builder builder) {
        super(builder);
        this.interestRate = builder.interestRate;
    }

    @Override
    public Long withdraw(Long withdrawValue) {
        if(withdrawValue > getBalance()){
            return 0L;
        }
        long finalBalance = getBalance() - withdrawValue;
        setBalance(finalBalance);
        return withdrawValue;
    }

    public static class Builder extends BankAccount.Builder {
        private Byte interestRate;

        public Builder interestRate(Byte interestRate) {
            this.interestRate = interestRate;
            return this;
        }

        @Override
        public BankAccount build() {
            return new SavingAccount(this);
        }
    }
}
