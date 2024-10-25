package org.example.dao;

public record accountMovement(MovementType movementType, Integer value) {

    public enum MovementType{
        WITHDRAWAL
        , DEPOSIT
    }
}
