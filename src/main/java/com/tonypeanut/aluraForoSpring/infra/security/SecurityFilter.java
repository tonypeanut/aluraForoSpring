package com.tonypeanut.aluraForoSpring.infra.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.tonypeanut.aluraForoSpring.domain.Usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            var authHeader = request.getHeader("Authorization");

            if(authHeader!=null){
                var token = authHeader.replace("Bearer ", "");
                var id = tokenService.getId(token);

                if(id!= null){
                    var usuario = usuarioRepository.findByIdAndEstado(id, "Activo")
                            .orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado con ID: " + id));
                    var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

            filterChain.doFilter(request, response);
        } catch (JWTVerificationException ex){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Error verificando el token");
        }
    }
}
