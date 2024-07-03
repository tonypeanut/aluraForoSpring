package com.tonypeanut.aluraForoSpring.domain.Usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    UserDetails findByUsuarioAndEstado(String usuario, String estado);

    boolean existsByUsuario(String usuario);

    boolean existsByEmail(String email);

    Page<Usuario> findAll(Pageable pageable);

    Optional<Usuario> findById(Long id);

    Optional<Usuario> findByIdAndEstado(Long id, String estado);

    boolean existsByUsuarioAndIdNot(String usuario, Long id);

    boolean existsByemailAndIdNot(String email, Long id);
}
