package com.tonypeanut.aluraForoSpring.domain.Respuesta;

import com.tonypeanut.aluraForoSpring.domain.Topico.Topico;
import com.tonypeanut.aluraForoSpring.domain.Topico.TopicoRepository;
import com.tonypeanut.aluraForoSpring.domain.Usuario.Usuario;
import com.tonypeanut.aluraForoSpring.domain.Usuario.UsuarioRepository;
import com.tonypeanut.aluraForoSpring.infra.errores.IdNotFoundException;
import com.tonypeanut.aluraForoSpring.infra.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RespuestaService {
    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    public Respuesta crearRespuesta(DatosRegistroRespuesta datos, HttpServletRequest request) {
        //Obtenemos el topico
        Topico topico = topicoRepository.findById(datos.topicoId())
                .orElseThrow(() -> new IdNotFoundException("Topico no encontrado"));

        //Obtenemos el usuario
        String authHeader = request.getHeader("Authorization");
        var token = authHeader.replace("Bearer ", "");
        Long idUsuario = tokenService.getId(token);
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(()-> new IdNotFoundException("Usuario no válido"));

        //Construimos la respuesta
        Respuesta respuesta = new Respuesta(datos, topico, usuario);

        return respuestaRepository.save(respuesta);
    }


    public Page<Respuesta> getRespuestasPorTopico(Long topicoId, Pageable paginacion) {
        return respuestaRepository.findByTopicoId(topicoId, paginacion);
    }

    /*

    public Optional<Respuesta> getRespuestaPorId(Long id) {
        return respuestaRepository.findById(id);
    }



    public Respuesta actualizarRespuesta(Long id, Respuesta respuestaActualizada) {
        return respuestaRepository.findById(id)
                .map(respuesta -> {
                    respuesta.setContenido(respuestaActualizada.getContenido());
                    return respuestaRepository.save(respuesta);
                })
                .orElseThrow(() -> new RuntimeException("Respuesta no encontrada"));
    }

    public void eliminarRespuesta(Long id) {
        respuestaRepository.deleteById(id);
    }

     */
}
