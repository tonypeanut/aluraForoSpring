package com.tonypeanut.aluraForoSpring.controller;

import com.tonypeanut.aluraForoSpring.domain.Curso.Curso;
import com.tonypeanut.aluraForoSpring.domain.Respuesta.*;
import com.tonypeanut.aluraForoSpring.domain.Topico.DatosRegistrarTopico;
import com.tonypeanut.aluraForoSpring.domain.Topico.DatosRespuestaTopico;
import com.tonypeanut.aluraForoSpring.domain.Topico.DatosRespuestaTopicoNuevo;
import com.tonypeanut.aluraForoSpring.domain.Topico.Topico;
import com.tonypeanut.aluraForoSpring.domain.Usuario.Usuario;
import com.tonypeanut.aluraForoSpring.infra.errores.CursoNotFoundException;
import com.tonypeanut.aluraForoSpring.infra.errores.UsuarioNotFoundException;
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
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/respuestas")
public class RespuestasController {
    @Autowired
    private RespuestaService respuestaService;

    // Registrar Respuesta
    @PostMapping
    @Transactional
    public ResponseEntity<DatosRespuestaRespuesta> crearRespuesta(
            @RequestBody @Valid DatosRegistroRespuesta respuesta,
            HttpServletRequest request
    ) {
        Respuesta nuevaRespuesta = respuestaService.crearRespuesta(respuesta, request);

        URI url = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(nuevaRespuesta.getId()).toUri();
        return ResponseEntity.created(url).body(new DatosRespuestaRespuesta(nuevaRespuesta));
    }


    // Listar Respuestas
    @GetMapping("/topico/{topicoId}")
    public ResponseEntity<Page<DatosRespuestaRespuesta>> obtenerRespuestasPorTopico(
            @PathVariable Long topicoId,
            @PageableDefault(size = 10) Pageable paginacion) {
        Page<Respuesta> respuestas = respuestaService.getRespuestasPorTopico(topicoId, paginacion);

        Page<DatosRespuestaRespuesta> respuestasPage = respuestas.map(DatosRespuestaRespuesta::new);

        return ResponseEntity.ok(respuestasPage);
    }


    // mostrar Respuesta por Id
    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaRespuesta> obtenerRespuestaPorId(
            @PathVariable Long id) {
        DatosRespuestaRespuesta datosRespuesta = respuestaService.getRespuestaPorId(id);
        return ResponseEntity.ok(datosRespuesta);
    }


    // Actualizar respuesta
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosRespuestaRespuesta> actualizarRespuesta(
            @PathVariable String id,
            @RequestBody @Valid DatosActualizarRespuesta datosRespuesta,
            HttpServletRequest request) {
        Respuesta respuesta = respuestaService.actualizarRespuesta(id, datosRespuesta, request);
        return ResponseEntity.ok(new DatosRespuestaRespuesta(respuesta));
    }

    // Eliminar respuesta
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminarRespuesta(@PathVariable String id, HttpServletRequest request) {
        respuestaService.eliminarRespuesta(id, request);
        return ResponseEntity.noContent().build();
    }
}
