package org.example.entity;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.entity.jsonView.BankAccountView;
import org.example.exception.UnacceptableMovementException;

import java.io.Serializable;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SavingAccount extends BankAccount implements Serializable {

    @JsonView(BankAccountView.Detailed.class)
    private Byte interestRate;

}
