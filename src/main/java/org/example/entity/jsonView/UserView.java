package org.example.entity.jsonView;

public interface UserView {

    interface Basic {}
    interface Detailed extends UserView.Basic,BankAccountView.Admin {}
    interface Admin extends UserView.Detailed {}

}
