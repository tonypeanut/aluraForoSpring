package com.tonypeanut.aluraForoSpring.domain.Respuesta;

import com.tonypeanut.aluraForoSpring.domain.Topico.Topico;
import com.tonypeanut.aluraForoSpring.domain.Usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name="respuestas")
@Entity(name="Respuesta")
@Data
public class Respuesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mensaje;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "respuestas_topicos_id")
    private Topico topico;
    private LocalDateTime fechaCreacion;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "respuestas_usuarios_id")
    private Usuario usuario;
    private String solucion;
}