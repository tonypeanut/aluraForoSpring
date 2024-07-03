package com.tonypeanut.aluraForoSpring.infra.errores;

public class CorreoDuplicadoException extends RuntimeException{
    public CorreoDuplicadoException(String message){
        super(message);
    }
}
