package org.example;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Scanner;

public class ContaTerminal {
    public static void main(String[] args) {

        DecimalFormat df = new DecimalFormat("#0.00");

        System.out.println("Bem vindo!");

        System.out.println("Criação de conta");

        Conta novaConta = new Conta();
        novaConta.setNumber(accountNumberInput());;
        novaConta.setAgency(accountAgencyInput());
        novaConta.setClientName(accountClientNameInput());
        novaConta.setBalance(accountBalanceInput());

        System.out.printf("Numero:\t\t%s%n", novaConta.getNumber());
        System.out.printf("Agencia:\t%s%n", novaConta.getAgency());
        System.out.printf("Nome:\t\t%s%n", novaConta.getClientName());
        System.out.printf("Saldo:\t\t%s%n", df.format(novaConta.getBalance()).replace(',','.'));

    }

    private static Integer accountNumberInput() {
        Integer numero = null;
            System.out.println("Digite o número da conta:");
        try {
            numero = Integer.parseInt(scannerInput());
            if(numero < 1) throw new NumberFormatException();
        } catch (NumberFormatException e){
            System.out.println("Número digitado inválido");
            numero = accountNumberInput();
        }
        return numero;
    }

    private static String accountAgencyInput() {
        String agencia = null;
        System.out.println("Digite o número de agencia:");
        agencia = scannerInput();
        if(agencia.isBlank()){
            System.out.println("Agencia invalida");
            agencia = accountAgencyInput();
        }
        return agencia;
    }

    private static String accountClientNameInput() {
        String name = null;
        System.out.println("Digite o nome de cliente:");
        name = scannerInput();
        if(name.isBlank()){
            System.out.println("nome invalido");
            name = accountClientNameInput();
        }
        return name;
    }

    private static Double accountBalanceInput() {
        Double saldo = null;
        System.out.println("Digite o saldo da conta:");
        try {
            String input = scannerInput();
            saldo = Double.parseDouble(input);
        } catch (NumberFormatException e){
            System.out.println("Número digitado inválido");
            saldo = accountBalanceInput();
        }
        return saldo;
    }

    private static String scannerInput() {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    public static class Conta{
       private Integer number;
       private String agency;
       private String clientName;
       private Double balance;

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

        public String getAgency() {
            return agency;
        }

        public void setAgency(String agency) {
            this.agency = agency;
        }

        public String getClientName() {
            return clientName;
        }

        public void setClientName(String clientName) {
            this.clientName = clientName;
        }

        public Double getBalance() {
            return balance;
        }

        public void setBalance(Double balance) {
            this.balance = balance;
        }
    }
}



/*
*
*   Atributo 	    Tipo 	    Exemplo
*   Numero 	        Inteiro 	1021
*   Agencia 	    Texto 	    067-8
*   Nome Cliente 	Texto 	    MARIO ANDRADE
*   Saldo 	        Decimal 	237.48
*
* */