package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.entity.jsonView.BalanceMovementView;
import org.example.entity.jsonView.BankAccountView;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@SuperBuilder
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AccountBalanceMovement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(BalanceMovementView.Admin.class)
    private Long id;

    @Column(nullable = false)
    @JsonView(BalanceMovementView.Basic.class)
    private Long movementValue;

    @Column
    @JsonView(BalanceMovementView.Admin.class)
    private LocalDateTime movementStartTime;

    @Column
    @JsonView(BalanceMovementView.Detailed.class)
    private LocalDateTime movementLastStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonView(BalanceMovementView.Detailed.class)
    private MovementType type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private MovementStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_account_id", nullable = true)
    @JsonView(BankAccountView.Basic.class)
    private UUID transferAccount;

    public enum MovementStatus{
        SUCCESS("Movimentação concluida com sucesso")
        ,STARTED("Movimentação iniciada")
        ,FAILED("Movimentação falhou")
        ;

        final String description;
        MovementStatus(String description) {
            this.description = description;
        }
    }

    public enum MovementType {
        PHYSICAL_WITHDRAW("saque físico")
        ,SENT_TRANSFER("movientação por transferencia enviada para outra conta registrada")
        ,RECEIVED_TRANSFER("movientação por recebida de outra conta registrada")
        ,MISC("outras movimentações diversas")
        ;
        private final String description;
        MovementType(String description) {
            this.description = description;
        }
    }

    public abstract BankAccount getBankAccount();

}
