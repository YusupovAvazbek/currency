package fido.currency.security;

import com.google.gson.Gson;
import fido.currency.dto.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtService {
    @Value("${spring.security.secret.key}")
    private String secretKey;
    @Autowired
    private Gson gson;
    public String generateToken(UserDto usersDto){
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*60*48))
                .setSubject(gson.toJson(usersDto))
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
    }
    public Claims claims(String token){
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
    public boolean isExpire(String token){
        return claims(token).getExpiration().getTime()<System.currentTimeMillis();
    }
    public UserDto subject(String token){
        return gson.fromJson(claims(token).getSubject(), UserDto.class);
    }
}
