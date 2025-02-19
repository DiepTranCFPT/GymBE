package com.gymsystem.cyber.config;

import com.gymsystem.cyber.service.TokenService;
import com.gymsystem.cyber.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import com.gymsystem.cyber.exception.AuthenticationHandler;
import com.gymsystem.cyber.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.Collection;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@PropertySource("classpath:application.properties")
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private final String[] PUBLIC_ENDPOINTS = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/api-docs/**",
            "/admin/login",
            "/admin/register",
            "/api/authen/login/**",
            "/api/test/public-api",
            "/api/authen/test/login",
            "/api/authen/profile",
            "/api/trainers/**",

            "/"
    };
    private final String[] PUBLIC_ENDPOINTS_METHOD = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/admin/register_PT",
            "/api/test/admin-api/**",
            "/api/authen/register/**"
    };

    final AuthenticationHandler authenticationHandler;
    final CustomJwtGrantedAuthoritiesConverter customJwtGrantedAuthoritiesConverter;
    final UserService userService;
    final TokenService tokenService;

    @Autowired
    public SecurityConfig(AuthenticationHandler authenticationHandler,
                          @Lazy CustomJwtGrantedAuthoritiesConverter customJwtGrantedAuthoritiesConverter, UserService userService, TokenService tokenService) {
        this.authenticationHandler = authenticationHandler;
        this.customJwtGrantedAuthoritiesConverter = customJwtGrantedAuthoritiesConverter;

        this.userService = userService;
        this.tokenService = tokenService;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
//                .authorizeHttpRequests(request -> request
//                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
//                        .requestMatchers(PUBLIC_ENDPOINTS_METHOD).hasRole("ADMIN")
//                        .anyRequest().authenticated()
//                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(authenticationHandler).accessDeniedHandler((request, response, accessDeniedException) -> {
                            authenticationHandler.handleAccessDeniedException(request, response);
                        }))
//                .authorizeHttpRequests(registry -> {
//                    registry.requestMatchers("/", "/api/authen/test/login").permitAll();
//                    registry.anyRequest().authenticated();
//                })
//                .oauth2Login(oauth2login -> {
//                    oauth2login
//                            .loginPage("/api/authen/test/login")
//                            .successHandler((request, response, authentication) -> response.sendRedirect("/api/authen/profile"));
//                })
                .userDetailsService(userService)
                .csrf(AbstractHttpConfigurer::disable);
        httpSecurity.addFilterBefore(new JwtAuthenticationFilter(tokenService, jwtDecoder(), userService),
                UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }


    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "HS512");
        return NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new CustomJwtGrantedAuthoritiesConverter());
        return converter;
    }


    @Bean
    public CustomJwtGrantedAuthoritiesConverter customJwtGrantedAuthoritiesConverter() {
        return new CustomJwtGrantedAuthoritiesConverter();
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

}
