package com.tonypeanut.aluraForoSpring.domain.Topico;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatosRegistrarTopico(
        @NotBlank(message = "El título no puede estar vacío")
        String titulo,
        @NotBlank(message = "El mensaje no puede estar vacío")
        String mensaje,
        @NotNull(message = "El id del curso debe ser número positivo y mayor a 0.")
        @Min(value = 1, message = "El id del curso debe ser número positivo y mayor a 0.")
        Long cursoId
) {
    public DatosRegistrarTopico(
                String titulo,
                String mensaje,
                Long cursoId) {
        this.titulo = titulo.trim();
        this.mensaje = mensaje.trim();
        this.cursoId = cursoId;
    }
}
