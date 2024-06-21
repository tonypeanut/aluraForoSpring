package com.tonypeanut.aluraForoSpring.infra.errores;

public class TituloDuplicadoException extends RuntimeException{
    public TituloDuplicadoException(String message) {
        super(message);
    }
}
