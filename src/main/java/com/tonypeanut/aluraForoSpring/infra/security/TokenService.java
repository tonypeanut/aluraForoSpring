package com.tonypeanut.aluraForoSpring.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tonypeanut.aluraForoSpring.domain.Usuario.Usuario;
import com.tonypeanut.aluraForoSpring.infra.errores.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

@Service
public class TokenService {

    @Value("${api.security.secret}")
    private String apiSecret;

    public String generarToken(Usuario usuario){
        try{
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            return JWT.create()
                    .withIssuer("ForoSpring")
                    .withSubject(usuario.getNombre())
                    .withClaim("id", usuario.getId())
                    .withExpiresAt(generarFechaExpiracion())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException();
        }
    }

    private Instant generarFechaExpiracion(){
        return LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.of("-07:00"));
    }

    public Long getId(String token){
        if(token == null){
            throw new RuntimeException("Token no puede ser nulo.");
        }
        DecodedJWT verifier = null;

        try{
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            verifier = JWT.require(algorithm)
                    .withIssuer("ForoSpring")
                    .build()
                    .verify(token);
            verifier.getSubject();
        } catch (JWTVerificationException exception){
            throw new JWTVerificationException("Error verificando el token.");
        }

        Claim idClaim = verifier.getClaim("id");
        if(idClaim.isNull()){
            throw new RuntimeException("Claim 'id' no encontrado en el token.");
        }

        return idClaim.asLong();
    }

    public void verificarAutor(HttpServletRequest request, Long id) {
        //Verificamos que el usuario del token sea el mismo que el ingresado como parametro
        var authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            var token = authHeader.replace("Bearer ", "");
            var idToken = getId(token);
            if (!Objects.equals(id, idToken)) {
                throw new UnauthorizedException("No estas autorizado para realizar esta acción.");
            }
        }
    }

    public Long obtenerIdUsuario(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            String token = authHeader.replace("Bearer ", "");
            return getId(token);
        } else {
            throw new UnauthorizedException("No estas autorizado para realizar esta acción.");
        }
    }
}
