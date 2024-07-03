package com.tonypeanut.aluraForoSpring.controller;


import com.tonypeanut.aluraForoSpring.domain.Usuario.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuariosController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    UsuarioRepository usuarioRepository;

    //Registrar usuario
    @PostMapping("/registrar")
    public ResponseEntity<DatosUsuarioRespuesta> registrarUsuario(@RequestBody @Valid DatosRegistrarUsuario datos) {
        DatosUsuarioRespuesta datosUsuarioRespuesta = usuarioService.crearUsuario(datos);
        return ResponseEntity.status(HttpStatus.CREATED).body(datosUsuarioRespuesta);
    }


    //Obtener lista de usuarios
    @GetMapping("/todos")
    public ResponseEntity<Page<DatosUsuarioRespuesta>> mostrarTodosLosUsuarios(@PageableDefault(size = 10) Pageable paginacion) {
        Pageable paginacionOrdenada = PageRequest.of(paginacion.getPageNumber(), paginacion.getPageSize(), Sort.by("email").ascending());
        Page<Usuario> listaUsuarios = usuarioRepository.findAll(paginacionOrdenada);
        Page<DatosUsuarioRespuesta> respuesta = listaUsuarios.map(DatosUsuarioRespuesta::new);
        return ResponseEntity.ok(respuesta);
    }

    //Obtener usuario por id
    @GetMapping("/{id}")
    public ResponseEntity<DatosUsuarioRespuesta> mostrarUsuarioPorId(@PathVariable String id) {
        var respuesta = usuarioService.mostrarUsuarioPorId(id);
        return ResponseEntity.ok(respuesta);
    }

    //Actualizar usuario
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosUsuarioRespuesta> actualizarUsuario(
            @PathVariable String id,
            @RequestBody @Valid DatosActualizarUsuario datos,
            HttpServletRequest request) {
        var respuesta = usuarioService.actualizarUsuario(id, datos, request);
        return ResponseEntity.ok(respuesta);
    }

    //Eliminar usuario
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminarUsuario(
            @PathVariable String id,
            HttpServletRequest request
    ){
        usuarioService.eliminarUsuario(id, request);
        return ResponseEntity.noContent().build();
    }

}
