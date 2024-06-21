package com.tonypeanut.aluraForoSpring.domain.Usuario;

import jakarta.persistence.*;
import lombok.*;

@Table(name="usuarios")
@Entity(name="Usuario")
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String email;
    private String password;
}
