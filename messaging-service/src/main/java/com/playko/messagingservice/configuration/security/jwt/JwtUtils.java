package com.playko.messagingservice.configuration.security.jwt;

import com.playko.messagingservice.configuration.security.exception.JwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

import java.util.List;

public class JwtUtils {
    private static final String jwtSecret = "eyTJhbGcdiOiJIUzUxMiIsInR5cCI6IkpaXVCJ9.eyJzdWIiOiIxMjM0NTY3sdODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.nZU_gPcMXkWpkCUpJceSxS7lSickF0tTImHhAR949Z-Nt69LgW8G6lid-mqd9B579tYM8C4FN2jdhR2VRMsjtA";

    public static List<String> getRoles(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(token).getBody();

        return claims.get("roles", List.class);
    }

    public static boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            throw new JwtException("Invalid Jwt signature");
        } catch (MalformedJwtException e) {
            throw new JwtException("Invalid Jwt token");
        } catch (ExpiredJwtException e) {
            throw new JwtException("JWT token is expired");
        } catch (UnsupportedJwtException e) {
            throw new JwtException("JWT token is unsupported");
        } catch (IllegalArgumentException e) {
            throw new JwtException("JWT claims string is empty");
        }
    }
}
