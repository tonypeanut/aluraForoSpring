package com.tonypeanut.aluraForoSpring.infra.errores;


public class UsuarioDuplicadoException extends RuntimeException {
    public UsuarioDuplicadoException(String mesage){
        super(mesage);
    }
}
