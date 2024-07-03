package com.tonypeanut.aluraForoSpring.infra.errores;

public class PasswordRepetidoException extends RuntimeException{
    public PasswordRepetidoException(String message){
        super(message);
    }
}
