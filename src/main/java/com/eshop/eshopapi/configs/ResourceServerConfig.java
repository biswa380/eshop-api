package com.eshop.eshopapi.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebSecurity
public class ResourceServerConfig {

  // @Value("${spring.security.resourceserver.jwt.issuer-uri}")
  private String issuerUri = "http://localhost:9000";
  private String jwkSetUri = "http://localhost:9000/oauth2/jwks";
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // http.securityMatcher("/categories/all", "/saveCategory/**", "/categoryTree/**", "/delete/**")
        http.securityMatcher("/category/**")  
        .authorizeHttpRequests(authorize -> authorize.anyRequest()
            .hasAuthority("SCOPE_openid"))
            .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(CsrfConfigurer::disable)
          .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder())));
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder
                    .withJwkSetUri(jwkSetUri)
                    .jwsAlgorithm(SignatureAlgorithm.RS256).build();
    }
}
