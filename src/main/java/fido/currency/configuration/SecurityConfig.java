package fido.currency.configuration;

import com.google.gson.Gson;
import fido.currency.dto.ApiResult;
import fido.currency.security.JwtFilter;
import fido.currency.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private JwtFilter jwtFilter;
    @Autowired
    private Gson gson;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .cors(cors -> cors.configurationSource(configurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(request -> request.requestMatchers(HttpMethod.POST, "/user").permitAll()
                        .requestMatchers("/user/login","/user/add").permitAll()
                        .requestMatchers("/currencies").permitAll()
                        .anyRequest()
                        .authenticated())
                .exceptionHandling(e -> e.authenticationEntryPoint(entryPoint()))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    private CorsConfigurationSource configurationSource() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedHeaders(List.of("Authorization", "Access-Control-Allow-Origin", "Content-Type"));
        cors.addAllowedMethod("*");
        cors.addAllowedOrigin("null");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);

        return source;
    }
    private AuthenticationEntryPoint entryPoint() {
        return (req, res, ex) -> {
            res.getWriter().println(gson.toJson(ApiResult.builder()
                    .message("Token is not valid: " + ex.getMessage() +
                            (ex.getCause() != null ? ex.getCause().getMessage() : ""))
                    .build()));
            res.setContentType("application/json");
            res.setStatus(400);
        };
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
