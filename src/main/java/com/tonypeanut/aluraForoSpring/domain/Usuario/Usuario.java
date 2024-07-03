package com.tonypeanut.aluraForoSpring.domain.Usuario;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Table(name="usuarios")
@Entity(name="Usuario")
@Data
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String usuario;
    private String nombre;
    private String email;
    private String password;
    private String estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    public Usuario(){

    }

    public Usuario(DatosRegistrarUsuario datos){
        this.usuario = datos.usuario();
        this.nombre = datos.nombre();
        this.email = datos.email();
        this.password = datos.password();
        this.fechaCreacion = LocalDateTime.now();
        this.estado = "Activo";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public String getUsername() {
        return usuario;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void Actualizar(DatosActualizarUsuario datos) {
        if (datos.nombre()!=null){
            this.nombre = datos.nombre();
        }

        if (datos.usuario()!=null){
            this.usuario = datos.usuario();
        }

        if (datos.email()!=null){
            this.email = datos.email();
        }

        this.fechaActualizacion = LocalDateTime.now();
    }

    public void desactivar(){
        this.estado = "Eliminado";
        this.fechaActualizacion = LocalDateTime.now();
    }
}
