package org.example.entity;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Entity;
import lombok.*;
import org.example.entity.jsonView.BankAccountView;
import org.example.exception.UnacceptableMovementException;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
public class CreditAccount extends BankAccount{

    @JsonView(BankAccountView.Detailed.class)
    private Byte taxRate;

    @JsonView(BankAccountView.Basic.class)
    private Long creditLimit;

}
