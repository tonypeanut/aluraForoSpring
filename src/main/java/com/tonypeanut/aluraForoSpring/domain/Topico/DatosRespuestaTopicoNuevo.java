package com.tonypeanut.aluraForoSpring.domain.Topico;

import java.time.LocalDateTime;

public record DatosRespuestaTopicoNuevo(
    Long id,
    String titulo,
    String mensaje,
    LocalDateTime fechaCreacion,
    String status,
    Long usuarioId,
    Long cursoId
){
    public DatosRespuestaTopicoNuevo(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getStatus(),
                topico.getUsuario().getId(),
                topico.getCurso().getId()
        );
    }
}
