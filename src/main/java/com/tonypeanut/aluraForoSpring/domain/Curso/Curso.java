package com.tonypeanut.aluraForoSpring.domain.Curso;

import jakarta.persistence.*;
import lombok.*;

@Table(name="cursos")
@Entity(name="Curso")
@Data
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    @Enumerated(EnumType.STRING)
    private CategoriaCurso categoria;
}
