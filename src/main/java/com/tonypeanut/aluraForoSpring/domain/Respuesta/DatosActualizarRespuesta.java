package com.tonypeanut.aluraForoSpring.domain.Respuesta;

import jakarta.validation.constraints.Size;

public record DatosActualizarRespuesta(
        @Size(min = 10, message = "El mensaje no puede ser muy corto.")
        String mensaje,
        String solucion
) {
}
