package org.example.entity;

import java.util.Objects;

public class TransactionAccount extends BankAccount{

    private Integer withdrawalLimit;

    public TransactionAccount(){}

    public TransactionAccount(CheckingAccountBuilder builder){
        super(builder);
        this.withdrawalLimit = builder.withdrawalLimit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TransactionAccount that = (TransactionAccount) o;
        return Objects.equals(withdrawalLimit, that.withdrawalLimit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), withdrawalLimit);
    }

    public static class CheckingAccountBuilder extends BankAccount.AccountBuilder{
        private Integer withdrawalLimit;

        public CheckingAccountBuilder withdrawalLimit(Integer withdrawalLimit) {
            this.withdrawalLimit = withdrawalLimit;
            return this;
        }

        @Override
        public BankAccount build() {
            return new TransactionAccount(this);
        }
    }

}
