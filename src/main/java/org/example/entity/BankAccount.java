package org.example.entity;

import jakarta.persistence.*;

import java.util.Objects;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    private String bankOrigin;
    private String number;
    private String agency;
    private User user;
    private Integer balance;

    public BankAccount() {}

    public BankAccount(AccountBuilder builder) {
        this.bankOrigin = builder.bankOrigin;
        this.agency = builder.agency;
        this.number = builder.number;
        this.user = builder.user;
        this.balance = builder.balance;
    }

    public String getBankOrigin() {
        return bankOrigin;
    }

    public void setBankOrigin(String bankOrigin) {
        this.bankOrigin = bankOrigin;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount that = (BankAccount) o;
        return Objects.equals(number, that.number) && Objects.equals(agency, that.agency) && Objects.equals(user, that.user) && Objects.equals(balance, that.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, agency, user, balance);
    }

    public abstract static class AccountBuilder {

        private String bankOrigin;
        private String number;
        private String agency;
        private User user;
        private Integer balance;

        public AccountBuilder bankOrigin(String bankOrigin) {
            this.bankOrigin = bankOrigin;
            return this;
        }

        public AccountBuilder number(String number) {
            this.number = number;
            return this;
        }

        public AccountBuilder agency(String agency) {
            this.agency = agency;
            return this;
        }

        public AccountBuilder user(User user) {
            this.user = user;
            return this;
        }

        public AccountBuilder balance(Integer balance) {
            this.balance = balance;
            return this;
        }

        public abstract BankAccount build();
    }

}
