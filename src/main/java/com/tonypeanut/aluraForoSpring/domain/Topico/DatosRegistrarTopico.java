package com.tonypeanut.aluraForoSpring.domain.Topico;

import com.tonypeanut.aluraForoSpring.domain.Curso.Curso;
import com.tonypeanut.aluraForoSpring.domain.Usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record DatosRegistrarTopico(
        @NotBlank(message = "El título no puede estar vacío")
        String titulo,
        @NotBlank(message = "El mensaje no puede estar vacío")
        String mensaje,
        @NotNull(message = "El id del autor debe ser número positivo y mayor a 0.")
        @Min(value = 1, message = "El id del autor debe ser número positivo y mayor a 0.")
        Long autorId,
        @NotNull(message = "El id del curso debe ser número positivo y mayor a 0.")
        @Min(value = 1, message = "El id del curso debe ser número positivo y mayor a 0.")
        Long cursoId
) {
    public DatosRegistrarTopico(
                String titulo,
                String mensaje,
                Long autorId,
                Long cursoId) {
        this.titulo = titulo.trim();
        this.mensaje = mensaje.trim();
        this.autorId = autorId;
        this.cursoId = cursoId;
    }
}
