package com.tonypeanut.aluraForoSpring.domain.Topico;

import com.tonypeanut.aluraForoSpring.domain.Curso.Curso;
import com.tonypeanut.aluraForoSpring.domain.Curso.CursoRepository;
import com.tonypeanut.aluraForoSpring.domain.Usuario.Usuario;
import com.tonypeanut.aluraForoSpring.domain.Usuario.UsuarioRepository;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

@Table(name="topicos")
@Entity(name="Topico")
@Data
public class Topico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String mensaje;
    private LocalDateTime fechaCreacion;
    private String status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topicos_usuarios_id")
    private Usuario usuario;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topicos_cursos_id")
    private Curso curso;

    public Topico(){

    }

    public Topico(DatosRegistrarTopico datos) {
        this.titulo = datos.titulo();
        this.mensaje = datos.mensaje();
        this.fechaCreacion = LocalDateTime.now();
        this.usuario = null;
        this.status = "Activo";
    }
}
