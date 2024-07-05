package com.tonypeanut.aluraForoSpring.domain.Respuesta;

import com.tonypeanut.aluraForoSpring.domain.Topico.Topico;
import com.tonypeanut.aluraForoSpring.domain.Topico.TopicoRepository;
import com.tonypeanut.aluraForoSpring.domain.Usuario.Usuario;
import com.tonypeanut.aluraForoSpring.domain.Usuario.UsuarioRepository;
import com.tonypeanut.aluraForoSpring.infra.errores.IdNotFoundException;
import com.tonypeanut.aluraForoSpring.infra.security.TokenService;
import com.tonypeanut.aluraForoSpring.util.UtilidadesService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    @Autowired
    private UtilidadesService utilidadesService;

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

    public DatosRespuestaRespuesta getRespuestaPorId(Long id) {
        Respuesta respuesta = respuestaRepository.findByIdAndEstado(id,"Activo")
                .orElseThrow(() -> new IdNotFoundException("Respuesta con el ID indicado no existe"));

        return new DatosRespuestaRespuesta(respuesta);
    }

    public Respuesta actualizarRespuesta(
            String id,
            DatosActualizarRespuesta datosActualizarRespuesta,
            HttpServletRequest request) {
        //Verificamos que el id tenga formato válido
        Long idLong = utilidadesService.verificarId(id);

        //Obtenemos la respuesta de la base de datos
        Respuesta respuesta = respuestaRepository.findByIdAndEstado(idLong,"Activo")
                .orElseThrow(()-> new IdNotFoundException("La respuesta no existe o fue eliminada."));

        //Verificamos que el usuario en el token y de la respuesta a actualizar sean el mismo
        tokenService.verificarAutor(request, respuesta.getUsuario().getId());

        //Actualizamos los datos y guardamos
        respuesta.actualizar(datosActualizarRespuesta);

        return respuestaRepository.save(respuesta);
    }

    public void eliminarRespuesta(String id, HttpServletRequest request) {
        //Primero verificamos que el id sea válido
        var idLong = utilidadesService.verificarId(id);

        //Obtenemos la respuesta
        Respuesta respuesta = respuestaRepository.findByIdAndEstado(idLong, "Activo")
                .orElseThrow(()-> new IdNotFoundException("Respuesta no encontrada o ya había sido eliminada."));

        //Validamos que el usuario sea quien desea eliminar el usuario
        tokenService.verificarAutor(request, respuesta.getUsuario().getId());

        //"Eliminamos" la respuesta desactivandola.
        respuesta.desactivar();
        respuestaRepository.save(respuesta);
    }
}
