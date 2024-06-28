package com.tonypeanut.aluraForoSpring.domain.Topico;

import jakarta.validation.Valid;

import java.time.LocalDateTime;

public record DatosRespuestaActualizarTopico(
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion,
        String status,
        Long usuarioId,
        Long cursoId
) {
    public DatosRespuestaActualizarTopico(Topico topico) {
        this(
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getFechaActualizacion(),
                topico.getStatus(),
                topico.getUsuario().getId(),
                topico.getCurso().getId()
        );
    }

    public DatosRespuestaActualizarTopico(@Valid DatosRespuestaActualizarTopico DatosRespuestaActualizarTopico){
        this(
                DatosRespuestaActualizarTopico.titulo(),
                DatosRespuestaActualizarTopico.mensaje(),
                DatosRespuestaActualizarTopico.fechaCreacion(),
                DatosRespuestaActualizarTopico.fechaActualizacion(),
                DatosRespuestaActualizarTopico.status(),
                DatosRespuestaActualizarTopico.usuarioId(),
                DatosRespuestaActualizarTopico.cursoId()
        );
    }
}
