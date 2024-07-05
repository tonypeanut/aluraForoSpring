package com.tonypeanut.aluraForoSpring.domain.Respuesta;

import com.tonypeanut.aluraForoSpring.domain.Topico.Topico;

import java.time.LocalDateTime;

public record DatosRespuestaRespuesta(
        Long topicoId,
        String mensaje,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion,
        String solucion,
        Long usuarioId
) {
    public DatosRespuestaRespuesta(Respuesta respuesta){
        this(
                respuesta.getTopico().getId(),
                respuesta.getMensaje(),
                respuesta.getFechaCreacion(),
                respuesta.getFechaActualizacion(),
                respuesta.getSolucion(),
                respuesta.getUsuario().getId()
        );
    }
}
