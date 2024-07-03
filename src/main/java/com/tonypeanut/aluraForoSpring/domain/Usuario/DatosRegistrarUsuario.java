package com.tonypeanut.aluraForoSpring.domain.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DatosRegistrarUsuario(
        @NotBlank(message = "El nombre no puede estar vacío.")
        String nombre,
        @Email(message = "El formato de correo electrónico no es correcto.")
        @NotBlank(message = "El campo correo electrónico no puede estar vacío.")
        String email,
        @NotBlank(message = "El campo usuario no puede estar vacío.")
        @Size(min = 6, message = "El campo usuario debe tener al menos 6 carácteres.")
        String usuario,
        @NotBlank(message = "El campo password no puede estar vacío.")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$",
                message = "El password debe tener al menos 8 caracteres e incluir un digito, una letra minuscula, una letra mayuscula y un carácter especial."
        )
        String password
) {
}
