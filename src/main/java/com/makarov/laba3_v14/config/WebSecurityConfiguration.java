package com.makarov.laba3_v14.config;

import com.makarov.laba3_v14.services.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {
    private final UserDetailsService userDetailsService;

    @Autowired
    public WebSecurityConfiguration(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests((requests) ->
            requests.requestMatchers("/api/**").authenticated()
                    .requestMatchers("/error", "/auth", "/logout", "/register", "/css/**", "/js/**", "/fonts/**").permitAll()
                    .anyRequest().authenticated()
        ).formLogin(form ->
            form.loginPage("/auth").permitAll()
                .defaultSuccessUrl("/")
        )
        .logout(logout ->
            logout.logoutUrl("/logout").logoutSuccessUrl("/auth")
        ).httpBasic(Customizer.withDefaults()).csrf(csrf -> csrf.disable()).build();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}
