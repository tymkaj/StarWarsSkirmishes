package pl.coderslab.starwarsapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import pl.coderslab.starwarsapp.advice.CustomAccessDeniedHandler;
import pl.coderslab.starwarsapp.advice.CustomAuthEntryPoint;
import pl.coderslab.starwarsapp.filter.CsrfHeaderExposeFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        return new HttpSessionCsrfTokenRepository();
    }

    @Bean
    SecurityFilterChain security(HttpSecurity http, CustomAccessDeniedHandler deniedHandler, CustomAuthEntryPoint authEntryPoint) throws Exception {
        http
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(deniedHandler)
                        .authenticationEntryPoint(authEntryPoint)
                )

                .csrf(csrf -> csrf
                        .csrfTokenRepository(new HttpSessionCsrfTokenRepository())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                        .ignoringRequestMatchers(
                                "/swApi/auth/login", "/swApi/auth/logout", "/swApi/auth/register"
                        )
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swApi/auth/login",
                                "/swApi/auth/register",
                                "/swApi/auth/session-info",
                                "/swApi/auth/csrf"
                        ).permitAll()
                        .requestMatchers("/swApi/**").authenticated()
                        .anyRequest().permitAll()
                )

                .sessionManagement(sm -> sm
                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                .headers(h -> h
                        .xssProtection(x -> {})
                        .frameOptions(f -> f.sameOrigin())
                        .contentTypeOptions(c -> {})
                )

                .addFilterAfter(new CsrfHeaderExposeFilter(), BasicAuthenticationFilter.class)
                .sessionManagement(sm -> sm
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .sessionFixation().migrateSession()
                )
                .httpBasic(b -> b.disable())
                .formLogin(f -> f.disable());

        return http.build();
    }

}
