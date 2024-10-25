package org.example.dao;

public record AccountMovement(MovementType movementType, Long value, Long accountId) {

    public enum MovementType{
        WITHDRAWAL
        , DEPOSIT
    }
}
