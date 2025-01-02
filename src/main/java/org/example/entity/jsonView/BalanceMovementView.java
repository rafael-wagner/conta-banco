package org.example.entity.jsonView;

public interface BalanceMovementView {

    interface Basic {}

    interface Detailed extends BalanceMovementView.Basic{}

    interface Admin extends  BalanceMovementView.Detailed{}
}
