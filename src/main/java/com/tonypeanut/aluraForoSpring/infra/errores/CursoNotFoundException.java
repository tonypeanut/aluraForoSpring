package com.tonypeanut.aluraForoSpring.infra.errores;

public class CursoNotFoundException extends RuntimeException{
    public CursoNotFoundException(String message) {
        super(message);
    }
}
