package security.example.security;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import security.example.auth.UserService;
import security.example.jwt.JwtConfig;
import security.example.jwt.JwtTokenVerifier;
import security.example.jwt.JwtUsernameAndPasswordAuthenticationFilter;

import javax.crypto.SecretKey;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static security.example.security.UserRole.STUDENT;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Configuration
@EnableWebSecurity
//this will enable @PreAuthorize on methods
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    PasswordEncoder passwordEncoder;
    UserService userService;
    JwtConfig jwtConfig;
    SecretKey secretKey;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, secretKey))
                .addFilterAfter(new JwtTokenVerifier(jwtConfig, secretKey), JwtUsernameAndPasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/api/**").hasAnyRole(STUDENT.name())
                .anyRequest()
                .authenticated();

//                .httpBasic();

//                .formLogin()
//                .loginPage("/login")
//                .permitAll()
//                .defaultSuccessUrl("/courses", true)
//                .passwordParameter("password") // by default
//                .usernameParameter("username") // by default
//                .and()
//                .rememberMe()
//                .tokenValiditySeconds(10)
//                .key("somethingverysecured")
//                .and()
//                .logout()
//                .logoutUrl("/logout")
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")) // this default when .csrf().disable()
//                .clearAuthentication(true) // this default when .csrf().disable()
//                .invalidateHttpSession(true) // this default when .csrf().disable()
//                .deleteCookies("remember-me", "JSESSIONID") // this default when .csrf().disable()
//                .logoutSuccessUrl("/login"); // this default when .csrf().disable()

    }

//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails annaSmith = User
//                .builder()
//                .username("student")
//                .password(passwordEncoder.encode("password"))
//                .authorities(STUDENT.getGrantedAuthority())
//                .build();
//        UserDetails linda = User
//                .builder()
//                .username("admin")
//                .password(passwordEncoder.encode("password"))
//                .authorities(ADMIN.getGrantedAuthority())
//                .build();
//        UserDetails tom = User
//                .builder()
//                .username("admintrainee")
//                .password(passwordEncoder.encode("password"))
//                .authorities(ADMINTRAINEE.getGrantedAuthority())
//                .build();
//
//        return new InMemoryUserDetailsManager(annaSmith, linda, tom);
//
//    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }
}
