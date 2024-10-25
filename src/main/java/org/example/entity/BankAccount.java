package org.example.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;
import org.example.entity.jsonView.BankAccountView;
import org.example.entity.jsonView.UserView;

import java.io.Serializable;

@Data
@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class BankAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(BankAccountView.Admin.class)
    private Long id;

    @JsonView(BankAccountView.Basic.class)
    private String bankOrigin;

    @JsonView(BankAccountView.Basic.class)
    private String number;

    @JsonView(BankAccountView.Basic.class)
    private String agency;

    @JsonView(BankAccountView.Basic.class)
    private Long balance;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "user_id")
    @JsonView(UserView.Admin.class)
    private User user;

    public BankAccount() {}

    public BankAccount(Builder builder) {
        this.bankOrigin = builder.bankOrigin;
        this.agency = builder.agency;
        this.number = builder.number;
        this.user = builder.user;
        this.balance = builder.balance;
    }

    /**
     * Subtrai da conta o valor passado no parametro
     * @param withdrawValue
     * @return withdrawValue ou '0', caso nada foi retirado
     */
    public abstract Long withdraw(Long withdrawValue);

    public void deposit(Long depositValue){
        setBalance(getBalance() + depositValue);
    }

    public abstract static class Builder {

        private String bankOrigin;
        private String number;
        private String agency;
        private User user;
        private Long balance;

        public Builder bankOrigin(String bankOrigin) {
            this.bankOrigin = bankOrigin;
            return this;
        }

        public Builder number(String number) {
            this.number = number;
            return this;
        }

        public Builder agency(String agency) {
            this.agency = agency;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder balance(Long balance) {
            this.balance = balance;
            return this;
        }

        public abstract BankAccount build();
    }

}
