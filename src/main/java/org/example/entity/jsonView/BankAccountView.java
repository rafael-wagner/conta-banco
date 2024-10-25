package org.example.entity.jsonView;

public interface BankAccountView {

    interface Basic {}
    interface Detailed extends Basic {}
    interface Admin extends Detailed {}
}
