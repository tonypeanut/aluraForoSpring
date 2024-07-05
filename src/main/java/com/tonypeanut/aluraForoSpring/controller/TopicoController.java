package com.tonypeanut.aluraForoSpring.controller;

import com.tonypeanut.aluraForoSpring.domain.Topico.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/topicos")
public class TopicoController {
    @Autowired
    private TopicoService topicoService;

    @GetMapping
    public ResponseEntity<Page<DatosRespuestaTopico>> mostrarTodosLosTopicos(
            @RequestParam(required = false) String cursoId,
            @RequestParam(required = false) Integer year,
            @PageableDefault(size = 10) Pageable paginacion){
        Page<DatosRespuestaTopico> respuesta = topicoService.mostrarTodosLosTopicos(cursoId, year, paginacion);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaTopico> mostrarTopicoPorId(@PathVariable String id){
        Topico topico = topicoService.mostrarTopicoPorId(id);
        return ResponseEntity.ok(new DatosRespuestaTopico(topico));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DatosRespuestaTopicoNuevo> registrarTopico(
            @RequestBody @Valid DatosRegistrarTopico datos,
            HttpServletRequest request){
        Topico topico =topicoService.registrarTopico(datos , request);
        URI url = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(url).body(new DatosRespuestaTopicoNuevo(topico));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosRespuestaActualizarTopico> actualizarTopico(
            @PathVariable String id,
            @Valid @RequestBody DatosActualizarTopico datos,
            HttpServletRequest request){
        Topico topicoActualizado = topicoService.actualizarTopico(id, datos, request);
        return ResponseEntity.ok(new DatosRespuestaActualizarTopico(topicoActualizado));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminarTopico(@PathVariable String id, HttpServletRequest request){
        topicoService.eliminarTopico(id, request);
        return ResponseEntity.noContent().build();
    }

}
