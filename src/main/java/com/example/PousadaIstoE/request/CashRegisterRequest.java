package com.example.PousadaIstoE.request;

public record CashRegisterRequest(
    String report,
    Integer apartment,
    Float cashIn,
    Float cashOut
    ){}
