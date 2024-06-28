package com.tonypeanut.aluraForoSpring.domain.Topico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicoRepository extends JpaRepository<Topico, Long> {
    @Query("SELECT t FROM Topico t WHERE t.titulo = :titulo AND t.mensaje = :mensaje")
    List<Topico> buscarTopicoYMensajeIguales(String titulo, String mensaje);

    @Query("SELECT t FROM Topico t WHERE FUNCTION('YEAR', t.fechaCreacion) = :year AND t.status = :status")
    Page<Topico> findByYear(Integer year, String status, Pageable pageable);

    @Query("SELECT t FROM Topico t JOIN t.curso c WHERE c.nombre LIKE %:curso% AND t.status = :status")
    Page<Topico> findByCurso(String curso, String status, Pageable pageable);

    @Query("SELECT t FROM Topico t JOIN t.curso c WHERE FUNCTION('YEAR', t.fechaCreacion) = :year AND c.nombre LIKE %:curso% AND t.status = :status" )
    Page<Topico> findByCursoAndYear(
            @Param("curso") String curso,
            @Param("year") Integer year,
            @Param("status") String status,
            Pageable pageable);

    @Query("SELECT t FROM Topico t WHERE t.titulo = :titulo AND t.mensaje = :mensaje AND t.id <> :id")
    List<Topico> buscarTopicoYMensajeIgualesExceptoId(String titulo, String mensaje, Long id);

    boolean existsByIdAndStatus(Long id, String status);

    Page<Topico> findAllByStatus(String status, Pageable pageable);
}
