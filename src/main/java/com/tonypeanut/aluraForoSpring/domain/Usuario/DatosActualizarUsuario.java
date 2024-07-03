package com.tonypeanut.aluraForoSpring.domain.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DatosActualizarUsuario(
        @Size(min = 10, message = "El nombre no puede tener menos de 10 caracteres.")
        String nombre,
        @Email(message = "El formato de correo electrónico no es correcto.")
        @Size(min = 10, message = "El campo correo electrónico no puede tener menos de 10 caracteres.")
        String email,
        @Size(min = 6, message = "El campo usuario debe tener al menos 6 carácteres.")
        String usuario,
        @Pattern(
             regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$",
             message = "El password nuevo debe tener al menos 8 caracteres e incluir un digito, una letra minuscula, una letra mayuscula y un carácter especial."
        )
        String passwordNuevo,
        @NotBlank
        @Size(min = 6, message = "El campo password debe tener al menos 8 carácteres.")
        String password
) {
    public DatosActualizarUsuario(
            String nombre,
            String email,
            String usuario,
            String passwordNuevo,
            String password
    ){
        if(nombre!=null){
            this.nombre = nombre.trim();
        } else {
            this.nombre = null;
        }

        if(email!=null){
            this.email = email.trim();
        } else {
            this.email = null;
        }

        if(usuario!=null){
            this.usuario = usuario.trim();
        } else {
            this.usuario = null;
        }

        if(passwordNuevo!= null){
            this.passwordNuevo = passwordNuevo.trim();
        } else {
            this.passwordNuevo = null;
        }

        if(password!= null){
            this.password = password.trim();
        } else {
            this.password = null;
        }

    }

}
