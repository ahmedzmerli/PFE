package com.example.GestionUser.security;

import com.example.GestionUser.entities.Role;
import com.example.GestionUser.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKeyBase64;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private byte[] getSigningKey() {
        return Base64.getDecoder().decode(secretKeyBase64);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<String,Object>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        // Récupérer la liste des rôles
        @SuppressWarnings("unchecked")
        List<String> roles = ((User) userDetails).getRoles().stream()
                .map(new java.util.function.Function<Role, String>() {
                    @Override
                    public String apply(Role role) {
                        return role.getName();
                    }
                })
                .distinct()
                .collect(Collectors.toList());

        extraClaims.put("roles", roles);
        return buildToken(extraClaims, userDetails);
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        // Récupérer la liste des authorities
        List<String> authorities = userDetails.getAuthorities().stream()
                .map(new java.util.function.Function<GrantedAuthority, String>() {
                    @Override
                    public String apply(GrantedAuthority ga) {
                        return ga.getAuthority();
                    }
                })
                .distinct()
                .collect(Collectors.toList());

        // Création du JWT
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .claim("authorities", authorities)
                .signWith(SignatureAlgorithm.HS256, getSigningKey())
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }
}
