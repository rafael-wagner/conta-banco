package org.example.entity;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.entity.jsonView.BankAccountView;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
public class CreditAccount extends BankAccount{

    @JsonView(BankAccountView.Detailed.class)
    private Byte taxRate;

    @JsonView(BankAccountView.Basic.class)
    private Long creditLimit;

    public CreditAccount() {
    }

    @Override
    public Long withdraw(Long withdrawValue) {
        long calcBalance = getBalance() - withdrawValue;

        if(Math.abs(calcBalance) > creditLimit){
            return 0L;
        }

        return withdrawValue;

    }

}
