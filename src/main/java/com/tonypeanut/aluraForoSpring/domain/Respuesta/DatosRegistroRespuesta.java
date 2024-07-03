package com.tonypeanut.aluraForoSpring.domain.Respuesta;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatosRegistroRespuesta(
        @NotNull(message = "No puede estar vacío")
        @Min(value = 1, message = "Tiene que ser entero y mayor a 0")
        Long topicoId,
        @NotBlank(message = "No puede estar vacío")
        String mensaje
) {
}
