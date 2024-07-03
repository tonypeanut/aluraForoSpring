package com.tonypeanut.aluraForoSpring.infra.errores;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(String mesage){
        super(mesage);
    }
}
