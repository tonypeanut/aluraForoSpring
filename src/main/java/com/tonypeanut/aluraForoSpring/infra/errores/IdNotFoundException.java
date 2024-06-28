package com.tonypeanut.aluraForoSpring.infra.errores;

public class IdNotFoundException extends RuntimeException{
    public IdNotFoundException(String message) {
        super(message);
    }
}
