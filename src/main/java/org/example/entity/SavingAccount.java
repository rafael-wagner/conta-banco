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

}
