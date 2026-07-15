package com.cadisson.cadisson.Configuration;

import com.cadisson.cadisson.Servicio.UsuarioDetallesService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            UsuarioDetallesService usuarioDetallesService,
            PasswordEncoder passwordEncoder
    ) {

        DaoAuthenticationProvider proveedor =
                new DaoAuthenticationProvider(
                        usuarioDetallesService
                );

        proveedor.setPasswordEncoder(
                passwordEncoder
        );

        return proveedor;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            DaoAuthenticationProvider proveedor
    ) throws Exception {

        http.authenticationProvider(proveedor);

        http.authorizeHttpRequests(
                autorizacion -> autorizacion

                        .requestMatchers(
                                "/",
                                "/login",
                                "/acceso-denegado",
                                "/error",
                                "/favicon.ico",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        )
                        .permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/cotizaciones/nuevo",
                                "/cotizaciones/confirmacion/**"
                        )
                        .permitAll()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/cotizaciones/guardar"
                        )
                        .permitAll()

                        .requestMatchers(
                                "/admin/**",
                                "/eventos/**",
                                "/servicios/**",
                                "/usuarios/**",
                                "/cotizaciones/**"
                        )
                        .hasRole("ADMIN")

                        .anyRequest()
                        .authenticated()
        );

        http.formLogin(
                formulario -> formulario

                        .loginPage("/login")

                        .loginProcessingUrl("/login")

                        .usernameParameter("correo")

                        .passwordParameter("password")

                        .defaultSuccessUrl(
                                "/admin",
                                true
                        )

                        .failureUrl(
                                "/login?error=true"
                        )

                        .permitAll()
        );

        http.logout(
                cierre -> cierre

                        .logoutUrl("/logout")

                        .logoutSuccessUrl(
                                "/?logout=true"
                        )

                        .invalidateHttpSession(true)

                        .clearAuthentication(true)

                        .deleteCookies("JSESSIONID")

                        .permitAll()
        );

        http.exceptionHandling(
                excepciones -> excepciones

                        .accessDeniedPage(
                                "/acceso-denegado"
                        )
        );

        return http.build();
    }
}