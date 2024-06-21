package com.tonypeanut.aluraForoSpring.domain.Perfil;

import jakarta.persistence.*;
import lombok.*;

@Table(name="perfiles")
@Entity(name="Perfil")
@Data
public class Perfil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private NombrePerfil nombre;
}
