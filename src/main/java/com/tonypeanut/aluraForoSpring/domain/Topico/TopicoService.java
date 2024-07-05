package com.tonypeanut.aluraForoSpring.domain.Topico;

import com.tonypeanut.aluraForoSpring.domain.Curso.Curso;
import com.tonypeanut.aluraForoSpring.domain.Curso.CursoRepository;
import com.tonypeanut.aluraForoSpring.domain.Usuario.Usuario;
import com.tonypeanut.aluraForoSpring.domain.Usuario.UsuarioRepository;
import com.tonypeanut.aluraForoSpring.infra.errores.CursoNotFoundException;
import com.tonypeanut.aluraForoSpring.infra.errores.IdNotFoundException;
import com.tonypeanut.aluraForoSpring.infra.errores.TituloYMensajeDuplicadosException;
import com.tonypeanut.aluraForoSpring.infra.errores.UsuarioNotFoundException;
import com.tonypeanut.aluraForoSpring.infra.security.TokenService;
import com.tonypeanut.aluraForoSpring.util.UtilidadesService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class TopicoService {
    @Autowired
    private TopicoRepository topicoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private CursoRepository cursoRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UtilidadesService utilidadesService;

    public Page<DatosRespuestaTopico> mostrarTodosLosTopicos(String cursoId,
                                                       Integer year,
                                                       Pageable paginacion){
        Pageable paginacionOrdenada = PageRequest.of(paginacion.getPageNumber(), paginacion.getPageSize(), Sort.by("fechaCreacion").ascending());
        Page<Topico> listaTopicos;

        if(cursoId!=null && year!=null){
            listaTopicos = topicoRepository.findByCursoAndYear(cursoId, year, "Activo", paginacionOrdenada);
        } else if (cursoId!=null){
            listaTopicos = topicoRepository.findByCurso(cursoId, "Activo", paginacionOrdenada);
        } else if (year !=null ){
            listaTopicos = topicoRepository.findByYear(year, "Activo", paginacionOrdenada);
        } else {
            listaTopicos = topicoRepository.findAllByStatus("Activo", paginacionOrdenada);
        }

        return listaTopicos.map(DatosRespuestaTopico::new);
    }

    public Topico mostrarTopicoPorId(
            String id){
        //Validamos el id
        Long idLong = verificarId(id);

        //Retornamos el topico referenciado
        return topicoRepository.getReferenceById(idLong);
    }

    public Topico registrarTopico(
            @RequestBody @Valid DatosRegistrarTopico datos,
            HttpServletRequest request){

        //Verificamos si existe un topico con mismos titulo y mensaje
        if(!topicoRepository.buscarTopicoYMensajeIguales(datos.titulo(), datos.mensaje()).isEmpty()){
            throw new TituloYMensajeDuplicadosException("Ya existe otro tópico con mismo título y mensaje.  \nTítulo: " + datos.titulo() + "\nMensaje: " + datos.mensaje());
        }

        //Obtenemos el id del usuario desde el token
        Long usuarioId = tokenService.obtenerIdUsuario(request);

        //Verificamos que curso exista y los obtenemos
        Usuario usuario = usuarioRepository.findByIdAndEstado(usuarioId, "Activo")
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con ID: " + usuarioId));
        Curso curso = cursoRepository.findById(datos.cursoId())
                .orElseThrow(() -> new CursoNotFoundException("Curso no encontrado con ID: " + datos.cursoId()));

        //Creamos el topico y asignamos curso y usuario
        var topico = new Topico(datos);
        topico.setUsuario(usuario);
        topico.setCurso(curso);

        return topicoRepository.save(topico);
    }

    public Topico actualizarTopico(
            String id,
            DatosActualizarTopico datos,
            HttpServletRequest request){

        //Validamos el id
        Long idLong = verificarId(id);

        //Obtenemos el topico
        Topico topico = topicoRepository.getReferenceById(idLong);

        //Verificamos que el usuario que desea actualizar el topico sea el mismo que lo creó.
        tokenService.verificarAutor(request, topico.getUsuario().getId());

        //Actualizamos el topico con los datos proporcionados ya validados
        topico.actualizar(datos);

        //Antes de guardar verificamos que el nuevo topico no coincida en titulo y mensaje con otro
        if(!topicoRepository.buscarTopicoYMensajeIgualesExceptoId(topico.getTitulo(), topico.getMensaje(), idLong).isEmpty()){
            throw new TituloYMensajeDuplicadosException("Ya existe otro tópico con mismo título y mensaje.  \nTítulo: " + topico.getTitulo() + "\nMensaje: " + topico.getMensaje());
        }

        //Guardamos los datos en la base de datos y lo retornamos
        return topicoRepository.save(topico);
    }

    public void eliminarTopico(
            @PathVariable String id,
            HttpServletRequest request){
        //Validamos el id
        Long idLong = verificarId(id);

        //Obtenemos el topico
        Topico topico = topicoRepository.getReferenceById(idLong);

        //Verificar que el topico sea eliminado por el autor
        tokenService.verificarAutor(request, topico.getUsuario().getId());

        //Marcamos el topico como eliminado y guardamos
        topico.desactivar();
        topicoRepository.save(topico);
    }

    //Función para verificar que el id tenga el formato adecuado y exista en la base de datos
    private Long verificarId(String id){
        //Primero verificamos que el id sea válido
        var idLong = utilidadesService.verificarId(id);

        //Verificamos que el id exista
        if(!topicoRepository.existsByIdAndStatus(idLong, "Activo")){
            throw new IdNotFoundException("El id no existe");
        }
        return idLong;
    }
}
