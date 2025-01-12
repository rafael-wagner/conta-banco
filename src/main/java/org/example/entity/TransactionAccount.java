package org.example.entity;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.entity.jsonView.BankAccountView;

import java.io.Serializable;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TransactionAccount extends BankAccount implements Serializable {

    @JsonView(BankAccountView.Detailed.class)
    private Long withdrawalLimit;

    @JsonView(BankAccountView.Detailed.class)
    private Byte withdrawalTax;

}
