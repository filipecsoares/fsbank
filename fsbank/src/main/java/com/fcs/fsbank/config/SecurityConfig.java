package com.fcs.fsbank.config;

import com.fcs.fsbank.exceptionhandler.CustomAccessDeniedHandler;
import com.fcs.fsbank.exceptionhandler.CustomBasicAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
import static org.springframework.security.config.Customizer.withDefaults;

@Profile("!prod")
@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(smc -> smc.invalidSessionUrl("/invalidSession").maximumSessions(5).maxSessionsPreventsLogin(false))
                .requiresChannel(rcc -> rcc.anyRequest().requiresInsecure())
                .authorizeHttpRequests(req -> req
                .requestMatchers("/myAccount", "/myBalance", "/myCards", "/myLoans").authenticated()
                .requestMatchers("/contact", "/notices", "/error", "/register").permitAll()
                .requestMatchers(toH2Console()).permitAll());
        http.csrf(AbstractHttpConfigurer::disable);
        http.headers(headers -> headers
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable
                )
        );
        http.formLogin(withDefaults());
        http.httpBasic(hbc -> hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        http.exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
}
