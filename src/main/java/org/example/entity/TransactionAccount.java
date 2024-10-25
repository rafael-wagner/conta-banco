package org.example.entity;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.entity.jsonView.BankAccountView;

import java.io.Serializable;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
public class TransactionAccount extends BankAccount implements Serializable {

    @JsonView(BankAccountView.Detailed.class)
    private Integer withdrawalLimit;

    @JsonView(BankAccountView.Detailed.class)
    private Byte withdrawalTax;

    public TransactionAccount() {
    }

    @Override
    public Long withdraw(Long withdrawValue) {
        if(withdrawValue > withdrawalLimit){
            withdrawValue = withdrawValue + withdrawValue*withdrawalTax;
        }

        if(withdrawValue > getBalance()){
            return 0L;
        }

        setBalance(getBalance() - withdrawValue);
        return withdrawValue;
    }

    public TransactionAccount(Builder builder){
        super(builder);
        this.withdrawalLimit = builder.withdrawalLimit;
        this.withdrawalTax = builder.withdrawalTax;
    }

    public static class Builder extends BankAccount.Builder {
        private Integer withdrawalLimit;
        private Byte withdrawalTax;

        public Builder withdrawalLimit(Integer withdrawalLimit) {
            this.withdrawalLimit = withdrawalLimit;
            return this;
        }

        public Builder withdrawalTax(Byte withdrawalTax) {
            this.withdrawalTax = withdrawalTax;
            return this;
        }

        @Override
        public TransactionAccount build() {
            return new TransactionAccount(this);
        }
    }

}
