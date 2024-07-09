package com.artissue.config;

import com.artissue.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/login", "/join", "/joinProc","/index","/veri/**","/qr/**").permitAll()
                        .requestMatchers("/contact","/blog","/store","/event","/elements","/findbyId", "/findPwdProc","/findIdProc", "/updatePassword", "/exhi/**","/reserve/**","/notice/**").permitAll()
                        .requestMatchers( "/css/**", "/js/**", "/images/**", "/bootstrap/**", "/ajax/**").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/my/**").hasAnyRole("ADMIN", "MEMBER")
                        .anyRequest().authenticated());


        http
                .formLogin((auth) -> auth
                        .loginPage("/login")
                        .loginProcessingUrl("/loginProc")
                        .successHandler(customAuthenticationSuccessHandler) // Custom Authentication Success Handler 설정
                        .permitAll())
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true));

        http
                .oauth2Login((oauth2) -> oauth2
                        .loginPage("/login")
                        .userInfoEndpoint((userInfoEndpointConfig) ->
                                userInfoEndpointConfig.userService(customOAuth2UserService)));

        http.csrf((auth) -> auth.disable());

        return http.build();
    }
}
