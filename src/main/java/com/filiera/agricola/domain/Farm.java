package com.filiera.agricola.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class Farm extends BusinessEntity {
    public Farm(
            String companyName,
            String VATNumber,
            String address,
            String email,
            String phoneNumber
    ){
        super(companyName,VATNumber,address,email,phoneNumber);
        registrationDate = LocalDateTime.now();
        authorizedUserIds = new ArrayList<UUID>();
    }
}
