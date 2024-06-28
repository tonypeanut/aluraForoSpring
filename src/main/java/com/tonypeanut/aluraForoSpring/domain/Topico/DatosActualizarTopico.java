package com.tonypeanut.aluraForoSpring.domain.Topico;

import jakarta.validation.constraints.Size;

public record DatosActualizarTopico(
        @Size(min = 10, message = "El título no puede estar vacío")
        String titulo,
        @Size(min = 10, message = "El mensaje no puede estar vacío")
        String mensaje
) {
    public DatosActualizarTopico(
            String titulo,
            String mensaje
    ) {
        if(titulo != null){
            this.titulo = titulo.trim();
        } else {
            this.titulo = null;
        }

        if(mensaje != null){
            this.mensaje = mensaje.trim();
        } else {
            this.mensaje = null;
        }
    }
}
