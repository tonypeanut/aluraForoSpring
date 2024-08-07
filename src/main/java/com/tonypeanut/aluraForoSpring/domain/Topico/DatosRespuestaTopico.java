package com.tonypeanut.aluraForoSpring.domain.Topico;

import java.time.LocalDateTime;

public record DatosRespuestaTopico(
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        String status,
        Long usuarioId,
        Long cursoId
) {
    public DatosRespuestaTopico(Topico topico) {
        this(
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getStatus(),
                topico.getUsuario().getId(),
                topico.getCurso().getId()
        );
    }
}
