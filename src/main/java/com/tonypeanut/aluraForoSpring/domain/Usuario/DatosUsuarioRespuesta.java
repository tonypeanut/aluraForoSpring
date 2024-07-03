package com.tonypeanut.aluraForoSpring.domain.Usuario;

public record DatosUsuarioRespuesta(
        String nombre,
        String email
) {
    public DatosUsuarioRespuesta(Usuario usuario){
        this(
                usuario.getNombre(),
                usuario.getEmail()
        );
    }
}
