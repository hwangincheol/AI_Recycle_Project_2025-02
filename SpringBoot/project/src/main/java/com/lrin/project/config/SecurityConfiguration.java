package com.lrin.project.config;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration {
    private final UserDetailsService userDetailsService;

    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        String[] urlsToBePermittedAll = {
            "/collect",
            "/contact/**",
            "/notices/**",
            "/register/**",
            "/invalidSession/**",
            "/login/**",
            "/logout/**",
            "/",
            "/css/**", // 반드시 추가할것
            "/error/**",
            "/img/**",
            "/signup",
            "/find",
            "/js/**",
            "/memberChk",
            "/memberSave",
            "/idpwChk",
            "/idFind",
            "/pwFind",
            "/password",
            "/pwSetting",
            "/mypage",
            "/price",
            "/board/list",
            "/board/detail/**",
            "/error",
            "/error/**"
        };

        /*한글 깨짐 방지*/
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        http.addFilterBefore(filter, CsrfFilter.class);

        /* @formatter:off */
        http.csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        );
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/error", "/error/**").permitAll()
                .requestMatchers(urlsToBePermittedAll).permitAll()
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
        );
        http.exceptionHandling(exception -> exception
                .accessDeniedPage("/error/403") // 권한 문제 발생 시 에러 페이지 이동
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "접근이 제한되었습니다.");
                })
        );
        http.formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/memberLogin")
                .usernameParameter("id")
                .passwordParameter("pw")
                .defaultSuccessUrl("/")
                .successHandler((request, response, authentication) -> {
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("text/html; charset=UTF-8");
                    response.sendRedirect("/");
                })
                .failureHandler((request, response, exception) -> {
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("text/html; charset=UTF-8");
                    response.sendRedirect("/login?error=true");
                })
        );
        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
        );
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }
}