package com.tonypeanut.aluraForoSpring.controller;


import com.tonypeanut.aluraForoSpring.domain.Curso.Curso;
import com.tonypeanut.aluraForoSpring.domain.Curso.CursoRepository;
import com.tonypeanut.aluraForoSpring.domain.Topico.DatosRegistrarTopico;
import com.tonypeanut.aluraForoSpring.domain.Topico.DatosRespuestaTopico;
import com.tonypeanut.aluraForoSpring.domain.Topico.Topico;
import com.tonypeanut.aluraForoSpring.domain.Topico.TopicoRepository;
import com.tonypeanut.aluraForoSpring.domain.Usuario.Usuario;
import com.tonypeanut.aluraForoSpring.domain.Usuario.UsuarioRepository;
import com.tonypeanut.aluraForoSpring.infra.errores.CursoNotFoundException;
import com.tonypeanut.aluraForoSpring.infra.errores.TituloDuplicadoException;
import com.tonypeanut.aluraForoSpring.infra.errores.UsuarioNotFoundException;
import jakarta.persistence.Transient;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;


import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private CursoRepository cursoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private TopicoRepository topicoRepository;

    @GetMapping
    public String getTopico(){
        return "Hello world";
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DatosRespuestaTopico> registrarTopico(@RequestBody @Valid DatosRegistrarTopico datos){

        boolean existeTopico = topicoRepository.existsByTitulo(datos.titulo());

        if(existeTopico){
            throw new TituloDuplicadoException("Ya existe otro tópico con el título: " + datos.titulo());
        }
        Usuario usuario = usuarioRepository.findById(datos.autorId())
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con ID: " + datos.autorId()));
        Curso curso = cursoRepository.findById(datos.cursoId())
                .orElseThrow(() -> new CursoNotFoundException("Curso no encontrado con ID: " + datos.cursoId()));

        var topico = new Topico(datos);
        topico.setUsuario(usuario);
        topico.setCurso(curso);

        topicoRepository.save(topico);

        var datosRespuestaTopico =new DatosRespuestaTopico(topico);
        URI url = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaTopico);
    }

}
