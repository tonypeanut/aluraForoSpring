package com.tonypeanut.aluraForoSpring.domain.Usuario;

import com.tonypeanut.aluraForoSpring.infra.errores.*;
import com.tonypeanut.aluraForoSpring.infra.security.TokenService;
import com.tonypeanut.aluraForoSpring.util.UtilidadesService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UtilidadesService utilidadesService;


    public Usuario crearUsuario(DatosRegistrarUsuario datos){

        //Verificamos si ya existe el usuario en la base de datos
        if (usuarioRepository.existsByUsuario(datos.usuario())){
            throw new UsuarioDuplicadoException("El nombre de usuario ya está en uso");
        }

        //Verificamos si ya existe el email en la base de datos
        if (usuarioRepository.existsByEmail(datos.email())){
            throw new CorreoDuplicadoException("El correo ya está en uso");
        }

        //Codificamos la contraseña
        String passwordCodificado = passwordEncoder.encode(datos.password());

        //Creamos el usuario y asignamos la contrsaseña
        var usuario = new Usuario(datos);
        usuario.setPassword(passwordCodificado);


        return usuarioRepository.save(usuario);
    }

    public Usuario mostrarUsuarioPorId(String id){
        //Verificamos que el id tenga formato válido
        Long idLong = utilidadesService.verificarId(id);

        //retornamos el usuario de la base de datos si está activo.
        return usuarioRepository.findByIdAndEstado(idLong,"Activo")
                .orElseThrow(() -> new IdNotFoundException("El usuario no existe en la base de datos o fue eliminado"));
    }

    public Usuario actualizarUsuario(String id, DatosActualizarUsuario datos, HttpServletRequest request){
        //Verificamos que el id tenga formato válido
        Long idLong = utilidadesService.verificarId(id);

        //Verificamos que si existe el usuario no exista otro igual en la base de datos.
        if (usuarioRepository.existsByUsuarioAndIdNot(datos.usuario(), idLong)){
            throw new UsuarioDuplicadoException("El usuario " + datos.usuario() + " ya existe.");
        }

        //Verificamos que si el email cambia éste no exista en la base de datos.
        if (usuarioRepository.existsByemailAndIdNot(datos.email(), idLong)){
            throw new CorreoDuplicadoException("El email " + datos.email() + " ya existe");
        }

        //Obtenemos el usuario de la base de datos
        Usuario usuario = usuarioRepository.findByIdAndEstado(idLong, "Activo")
                .orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado o no está activo"));

        //Verificar que el usuario coincida con el del token
        tokenService.verificarAutor(request, usuario.getId());

        //Verificamos que la contraseña sea correcta
        if(!passwordEncoder.matches(datos.password(), usuario.getPassword())){
            throw new UnauthorizedException("Password inválido");
        }

        //Si hay password nuevo
        if(datos.passwordNuevo()!=null) {
            //Verificamos que estén presentes la contraseña actual y la nueva y que no sean iguales
            if (datos.passwordNuevo().equals(datos.password())) {
                throw new PasswordRepetidoException("Los password son iguales");
            }
            //Codificamos la contraseña nueva
            var passwordNuevoCodificado = passwordEncoder.encode(datos.passwordNuevo());
            usuario.setPassword(passwordNuevoCodificado);
        }

        //Actualizamos los datos y guardamos
        usuario.Actualizar(datos);
        return usuarioRepository.save(usuario);
    }

    public void eliminarUsuario(String id, HttpServletRequest request){
        //Verificamos que el id tenga formato válido
        Long idLong = utilidadesService.verificarId(id);

        //Validamos que el usuario sea quien desea eliminar el usuario
        tokenService.verificarAutor(request, idLong);

        // Verificar si el usuario existe
        Usuario usuario = usuarioRepository.findByIdAndEstado(idLong, "Activo")
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado o ya fue eliminado"));

        // Eliminar el usuario
        usuario.desactivar();
        usuarioRepository.save(usuario);
    }
}
