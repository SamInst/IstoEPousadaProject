package com.example.PousadaIstoE.exceptions;
public class EntityNotFound extends RuntimeException{
    public EntityNotFound(String message){
        super(message);
    }
}