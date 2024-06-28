package com.tonypeanut.aluraForoSpring.infra.errores;

public class TituloYMensajeDuplicadosException extends RuntimeException{
    public TituloYMensajeDuplicadosException(String message) {
        super(message);
    }
}
