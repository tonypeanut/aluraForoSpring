package com.tonypeanut.aluraForoSpring.domain.Topico;

import com.tonypeanut.aluraForoSpring.domain.Curso.Curso;
import com.tonypeanut.aluraForoSpring.domain.Curso.CursoRepository;
import com.tonypeanut.aluraForoSpring.domain.Usuario.Usuario;
import com.tonypeanut.aluraForoSpring.domain.Usuario.UsuarioRepository;
import com.tonypeanut.aluraForoSpring.infra.errores.CursoNotFoundException;
import com.tonypeanut.aluraForoSpring.infra.errores.IdNotFoundException;
import com.tonypeanut.aluraForoSpring.infra.errores.TituloYMensajeDuplicadosException;
import com.tonypeanut.aluraForoSpring.infra.errores.UsuarioNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicoService {
    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    public Long verificarIdTopico(String id, String mensaje){
        var idNotfoundException = new IdNotFoundException(mensaje);

        Long idLong;
        try{
            idLong = Long.valueOf(id);
        } catch (Exception e){
            throw idNotfoundException;
        }

        Boolean topicoExiste = topicoRepository.existsByIdAndStatus(idLong, "Activo");

        if(!topicoExiste){
            throw idNotfoundException;
        }

        return idLong;
    }

    public void verificarTituloAndMensajeDuplicados(String titulo, String mensaje){
        System.out.println("Titulo " + titulo);
        System.out.println("Mensaje " + mensaje);
        var existeTopico = topicoRepository.buscarTopicoYMensajeIguales(titulo, mensaje);
        System.out.println(existeTopico);

        if(!existeTopico.isEmpty()){
            throw new TituloYMensajeDuplicadosException("Ya existe otro tópico con mismo título y mensaje.  \nTítulo: " + titulo + "\nMensaje: " + mensaje);
        }
    }

    public void verificarTituloYMensajeDuplicadoExceptoIdActual(String titulo, String mensaje, Long id){
        if (titulo == null || titulo.isBlank() || mensaje == null || mensaje.isBlank()){
            return;
        }
        var existeTopico = topicoRepository.buscarTopicoYMensajeIgualesExceptoId(titulo, mensaje, id);

        if(!existeTopico.isEmpty()){
            throw new TituloYMensajeDuplicadosException("Ya existe otro tópico con mismo título y mensaje.  \nTítulo: " + titulo + "\nMensaje: " + mensaje);
        }
    }

}
