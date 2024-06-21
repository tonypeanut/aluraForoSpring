package com.tonypeanut.aluraForoSpring.infra.errores;

public class UsuarioNotFoundException extends RuntimeException {
    public UsuarioNotFoundException(String message) {
        super(message);
    }
}
