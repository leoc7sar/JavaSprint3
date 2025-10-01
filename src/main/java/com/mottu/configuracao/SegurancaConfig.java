package com.mottu.configuracao;

import com.mottu.seguranca.DetalhesUsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SegurancaConfig {

    @Autowired
    private DetalhesUsuarioServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
          .authorizeHttpRequests(auth -> auth
              .requestMatchers("/css/**","/js/**","/images/**","/webjars/**","/h2-console/**","/login","/error").permitAll()
              .requestMatchers("/admin/**").hasRole("ADMIN")
              .requestMatchers("/locacoes/**","/pagamentos/**").hasAnyRole("ADMIN","USUARIO")
              .anyRequest().authenticated()
          )
          .formLogin(form -> form
              .loginPage("/login")
              .permitAll()
          )
          .logout(logout -> logout
              .logoutUrl("/logout")
              .logoutSuccessUrl("/login?logout")
              .permitAll()
          )
          .authenticationProvider(authenticationProvider())
          // enable H2 console frames in dev
          .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
          .headers(headers -> headers.frameOptions().sameOrigin())
          ;
        return http.build();
    }
}