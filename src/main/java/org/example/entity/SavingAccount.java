package org.example.entity;

import java.util.Objects;

public class SavingAccount extends BankAccount{

    private Integer interestRate;

    public Integer getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Integer interestRate) {
        this.interestRate = interestRate;
    }

    public SavingAccount(SavingAccountBuilder builder) {
        super(builder);
        this.interestRate = builder.interestRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SavingAccount that = (SavingAccount) o;
        return Objects.equals(interestRate, that.interestRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), interestRate);
    }

    public static class SavingAccountBuilder extends BankAccount.AccountBuilder{
        private Integer interestRate;

        public SavingAccountBuilder setInterestRate(Integer interestRate) {
            this.interestRate = interestRate;
            return this;
        }

        @Override
        public BankAccount build() {
            return new SavingAccount(this);
        }
    }
}
