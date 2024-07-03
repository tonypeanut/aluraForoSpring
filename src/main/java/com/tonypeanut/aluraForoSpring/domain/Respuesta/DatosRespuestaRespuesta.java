package com.tonypeanut.aluraForoSpring.domain.Respuesta;

import com.tonypeanut.aluraForoSpring.domain.Topico.Topico;

import java.time.LocalDateTime;

public record DatosRespuestaRespuesta(
        Long topicoId,
        String mensaje,
        LocalDateTime fechaCreacion,
        String solucion,
        Long usuarioId
) {
    public DatosRespuestaRespuesta(Respuesta respuesta){
        this(
                respuesta.getTopico().getId(),
                respuesta.getMensaje(),
                respuesta.getFechaCreacion(),
                respuesta.getSolucion(),
                respuesta.getUsuario().getId()
        );
    }
}
