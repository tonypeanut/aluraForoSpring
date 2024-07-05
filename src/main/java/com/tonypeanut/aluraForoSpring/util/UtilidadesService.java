package com.tonypeanut.aluraForoSpring.util;

import com.tonypeanut.aluraForoSpring.infra.errores.IdNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UtilidadesService {
    public Long verificarId(String id){
        Long idLong;
        try {
            idLong = Long.valueOf(id);
        } catch (Exception e){
            throw new IdNotFoundException("El Id " + id + " no es válido o no existe.");
        }
        return idLong;
    }
}
