package com.tonypeanut.aluraForoSpring.domain.Usuario;

import com.tonypeanut.aluraForoSpring.infra.errores.*;
import com.tonypeanut.aluraForoSpring.infra.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;


    public DatosUsuarioRespuesta crearUsuario(DatosRegistrarUsuario datos){

        if (usuarioRepository.existsByUsuario(datos.usuario())){
            throw new UsuarioDuplicadoException("El nombre de usuario ya está en uso");
        }

        if (usuarioRepository.existsByEmail(datos.email())){
            throw new CorreoDuplicadoException("El correo ya está en uso");
        }

        String passwordCodificado = passwordEncoder.encode(datos.password());
        var usuario = new Usuario(datos);
        usuario.setPassword(passwordCodificado);
        var usuarioGuardado = usuarioRepository.save(usuario);
        return new DatosUsuarioRespuesta(usuarioGuardado);
    }

    public DatosUsuarioRespuesta mostrarUsuarioPorId(String id){
        Long idLong;
        var idNotFoundException = new IdNotFoundException("El Id " + id + " no existe.");
        try{
            idLong = Long.valueOf(id);
        } catch (Exception e){
            throw idNotFoundException;
        }

        Usuario usuario = usuarioRepository.findByIdAndEstado(idLong,"Activo")
                .orElseThrow(() -> idNotFoundException);

        return new DatosUsuarioRespuesta(usuario);
    }

    public DatosUsuarioRespuesta actualizarUsuario(String id, DatosActualizarUsuario datos, HttpServletRequest request){

        //Verificamos que el id sea válido
        Long idLong;
        try {
            idLong = Long.valueOf(id);
        } catch (Exception e){
            throw new IdNotFoundException("El Id " + id + " no existe.");
        }

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
        var authHeader = request.getHeader("Authorization");
        if(authHeader!=null){
            var token = authHeader.replace("Bearer ", "");
            var idToken = tokenService.getId(token);
            if(!Objects.equals(usuario.getId(), idToken)){
                throw new UnauthorizedException("No estas autorizado para realizar esta actualización");
            }
        }

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
        Usuario usuarioActualizado = usuarioRepository.save(usuario);

        return new DatosUsuarioRespuesta(usuarioActualizado);
    }

    public void eliminarUsuario(String id, HttpServletRequest request){
        //Verificamos que el id sea válido
        Long idLong;
        try {
            idLong = Long.valueOf(id);
        } catch (Exception e){
            throw new IdNotFoundException("El Id " + id + " no existe.");
        }

        // Obtener el token del encabezado Authorization
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Token no presente o mal formado.");
        }

        //Obtenemos el id del token
        var token = authHeader.replace("Bearer ", "");
        Long idToken = tokenService.getId(token);

        //Validamos el id del path
        Long idPath;
        try{
            idPath = Long.valueOf(id);
        } catch (Exception e){
            throw new IdNotFoundException("Id no válido");
        }

        //Comparamos id del token con el path
        if(!idToken.equals(idPath)){
            throw new UnauthorizedException("No estas autorizado para realizar esta actualización");
        }

        // Verificar si el usuario existe
        Usuario usuario = usuarioRepository.findByIdAndEstado(idLong, "Activo")
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado o ya fue eliminado"));

        // Eliminar el usuario
        usuario.desactivar();
        usuarioRepository.save(usuario);
    }
}
