package org.example.exception;

import lombok.Getter;

public class UnacceptableMovementException extends RuntimeException {

    public UnacceptableMovementException(String message) {
        super(message);
    }

    public UnacceptableMovementException(Reason reason) {
        super(reason.description);
    }

    @Getter
    public enum Reason{
        EXCEEDING_CREDIT_LIMIT("Movimento excede o valor limite")
        ,EXCEEDING_BALANCE_LIMIT("Movimento excede o valor de saldo limite")
        ,BELOW_MINIMUM_VALUE("Valor de movimento abaixo do esperado")
        ,OTHER_UNACCEPTABLE_MOVEMENT("Movimento não foi aceite por outros motivos")

        ;

        final String description;

        Reason(String description) {
            this.description = description;
        }
    }

}
