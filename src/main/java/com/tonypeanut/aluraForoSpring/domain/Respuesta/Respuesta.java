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
    private LocalDateTime fechaActualizacion;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "respuestas_usuarios_id")
    private Usuario usuario;
    private String solucion;
    private String estado;

    public Respuesta(){}

    public Respuesta(
            DatosRegistroRespuesta datos,
            Topico topico,
            Usuario usuario
    ) {
        this.mensaje = datos.mensaje();
        this.topico = topico;
        this.fechaCreacion = LocalDateTime.now();
        this.usuario = usuario;
        this.solucion = "No";
        this.estado = "Activo";
    }

    public void actualizar(DatosActualizarRespuesta datos){
        if(datos.mensaje()!=null){
            this.mensaje = datos.mensaje();
        }

        if(datos.solucion() != null && datos.solucion().equals("Solucionado")){
            this.solucion = "Solucionado";
        }

        this.fechaActualizacion = LocalDateTime.now();
    }

    public void desactivar(){
        this.estado = "Eliminado";
        this.fechaActualizacion = LocalDateTime.now();
    }
}

