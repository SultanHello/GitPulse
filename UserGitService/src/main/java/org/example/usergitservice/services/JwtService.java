package org.example.usergitservice.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.usergitservice.models.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    private final String SECRET_KEY ="4b4d109b8a545947e3d06e7856aad9a97f546e19330280128170df02bbe3e211";
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 24 * 1000))
                .signWith(getSignKey())
                .compact();
    }
    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String extractId(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public  <T>T extractClaim(String token, Function<Claims,T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return  Jwts.parserBuilder()
                .setSigningKey(getSignKey())  // Установка секретного ключа для подписи
                .build()
                .parseClaimsJws(token)  // Разбор токена и проверка подписи
                .getBody();
    }
    public boolean isValid(String token, UserDetails user){
        String idNumber = extractId(token);
        return idNumber.equals(user.getUsername())&& !isValidExtract(token);
    }

    public void validateToken(String token) {
        Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build().parseClaimsJws(token);
    }



    public boolean isValidExtract(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }


}
