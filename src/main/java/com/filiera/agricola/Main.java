package com.filiera.agricola;

import com.filiera.agricola.domain.BusinessEntity;
import com.filiera.agricola.domain.Farm;

public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.println("Start program!");

        BusinessEntity farm1 = new Farm(
            "Azienda Agricola Rossi",
            "IT12345678901",
            "Via dei Campi 123, Milano",
            "info@farmrossi.it",
            "+39 02 1234567"
        );

        System.out.println("Farm1: " + farm1.getAddress());
    }
}