package org.example.entity;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Entity;
import lombok.*;
import org.example.entity.jsonView.BankAccountView;
import org.example.exception.UnacceptableMovementException;

import java.io.Serializable;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavingAccount extends BankAccount implements Serializable {

    @JsonView(BankAccountView.Detailed.class)
    private Byte interestRate;

    @Override
    public void withdraw(Long withdrawValue) throws UnacceptableMovementException {
        if (withdrawValue > getBalance()) {
            throw new UnacceptableMovementException(UnacceptableMovementException.Reason.EXCEEDING_BALANCE_LIMIT);
        }
        this.setBalance(getBalance() - withdrawValue);
    }

}
