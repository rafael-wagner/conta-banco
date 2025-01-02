package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.AccountBalanceMovement;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BalanceMovementInfoDto {
    private Long value;
    private Long originAccountNumber;
    private Long destinationAccountNumber;
    private AccountBalanceMovement.MovementType movementType;
}

