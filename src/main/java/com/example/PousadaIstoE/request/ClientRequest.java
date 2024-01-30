package com.example.PousadaIstoE.request;

import java.time.LocalDate;

public record ClientRequest(
        String name,
        String cpf,
        String phone,
        LocalDate birth,
        String address,
        String job
) {
}
