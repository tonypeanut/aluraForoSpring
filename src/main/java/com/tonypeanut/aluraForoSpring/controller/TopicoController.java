package com.tonypeanut.aluraForoSpring.controller;

import com.tonypeanut.aluraForoSpring.domain.Curso.Curso;
import com.tonypeanut.aluraForoSpring.domain.Curso.CursoRepository;
import com.tonypeanut.aluraForoSpring.domain.Topico.*;
import com.tonypeanut.aluraForoSpring.domain.Usuario.Usuario;
import com.tonypeanut.aluraForoSpring.domain.Usuario.UsuarioRepository;
import com.tonypeanut.aluraForoSpring.infra.errores.CursoNotFoundException;
import com.tonypeanut.aluraForoSpring.infra.errores.IdNotFoundException;
import com.tonypeanut.aluraForoSpring.infra.errores.TituloYMensajeDuplicadosException;
import com.tonypeanut.aluraForoSpring.infra.errores.UsuarioNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private CursoRepository cursoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private TopicoRepository topicoRepository;
    @Autowired
    private TopicoService topicoService;

    @GetMapping
    public ResponseEntity<Page<DatosRespuestaTopico>> mostrarTodosLosTopicos(
            @RequestParam(required = false) String nombreCurso,
            @RequestParam(required = false) Integer year,
            @PageableDefault(size = 10) Pageable paginacion){

        Pageable paginacionOrdenada = PageRequest.of(paginacion.getPageNumber(), paginacion.getPageSize(), Sort.by("fechaCreacion").ascending());
        Page<Topico> listaTopicos;

        if(nombreCurso!=null && year!=null){
            listaTopicos = topicoRepository.findByCursoAndYear(nombreCurso, year, "Activo", paginacionOrdenada);
        } else if (nombreCurso!=null){
            listaTopicos = topicoRepository.findByCurso(nombreCurso, "Activo", paginacionOrdenada);
        } else if (year !=null ){
            listaTopicos = topicoRepository.findByYear(year, "Activo", paginacionOrdenada);
        } else {
            listaTopicos = topicoRepository.findAllByStatus("Activo", paginacionOrdenada);
        }

        Page<DatosRespuestaTopico> respuesta = listaTopicos.map(DatosRespuestaTopico::new);

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaTopico> mostrarTopicoPorId(@PathVariable String id){
        var idLong = topicoService.verificarIdTopico(id, "El tópico solicitado no existe en la base de datos");
        Topico topico = topicoRepository.getReferenceById(idLong);
        return ResponseEntity.ok(new DatosRespuestaTopico(topico));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DatosRespuestaTopicoNuevo> registrarTopico(@RequestBody @Valid DatosRegistrarTopico datos){

        //Verificamos si existe un topico con mismos titulo y mensaje
        topicoService.verificarTituloAndMensajeDuplicados(datos.titulo(), datos.mensaje());

        //Verificamos que usuario y curso existan
        Usuario usuario = usuarioRepository.findById(datos.autorId())
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con ID: " + datos.autorId()));
        Curso curso = cursoRepository.findById(datos.cursoId())
                .orElseThrow(() -> new CursoNotFoundException("Curso no encontrado con ID: " + datos.cursoId()));

        var topico = new Topico(datos);
        topico.setUsuario(usuario);
        topico.setCurso(curso);

        topicoRepository.save(topico);

        var datosRespuestaTopico =new DatosRespuestaTopicoNuevo(topico);
        URI url = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaTopico);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosRespuestaActualizarTopico> actualizarTopico(
            @PathVariable String id,
            @Valid @RequestBody DatosActualizarTopico datos){

        //Primero verificamos que exista el id
        var idLong = topicoService.verificarIdTopico(id, "El tópico que desea actualizar no existe en la base de datos");

        //Obtenemos el topico
        Topico topico = topicoRepository.getReferenceById(idLong);

        //Actualizamos el topico con los datos proporcionados ya validados
        topico.actualizar(datos);

        //Antes de guardar verificamos que el nuevo topico no coincida en titulo y mensaje con otro
        topicoService.verificarTituloYMensajeDuplicadoExceptoIdActual(topico.getTitulo(), topico.getMensaje(), idLong);

        //Guardamos los datos en la base de datos
        var topicoActualizado = topicoRepository.save(topico);

        return ResponseEntity.ok(new DatosRespuestaActualizarTopico(topicoActualizado));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarTopico(@PathVariable String id){
        var idLong = topicoService.verificarIdTopico(id, "El topico que desea eliminar no existe en la base de datos");
        var topico = topicoRepository.getReferenceById(idLong);
        topico.desactivar();
        topicoRepository.save(topico);
        return ResponseEntity.noContent().build();
    }

}
