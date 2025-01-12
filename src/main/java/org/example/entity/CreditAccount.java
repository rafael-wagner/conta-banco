package org.example.entity;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.entity.jsonView.BankAccountView;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class CreditAccount extends BankAccount{

    @JsonView(BankAccountView.Detailed.class)
    private Byte taxRate;

    @JsonView(BankAccountView.Basic.class)
    private Long creditLimit;

}
