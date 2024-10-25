package org.example.entity.jsonView;

public interface UserView {

    interface Detailed extends BankAccountView.Admin {}
    interface Admin extends UserView.Detailed {}

}
