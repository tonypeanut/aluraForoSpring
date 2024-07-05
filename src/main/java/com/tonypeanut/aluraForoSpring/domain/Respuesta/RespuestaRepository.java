package com.tonypeanut.aluraForoSpring.domain.Respuesta;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RespuestaRepository extends JpaRepository<Respuesta, Long> {
    Page<Respuesta> findByTopicoId(Long topicoId, Pageable pageable);

    Optional<Respuesta> findByIdAndEstado(Long id, String estado);
}
