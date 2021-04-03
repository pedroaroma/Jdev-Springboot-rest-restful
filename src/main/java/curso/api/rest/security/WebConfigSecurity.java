package curso.api.rest.security;

import curso.api.rest.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/*Mapeia URL Endereços autoriza ou bloqueia acessos a URL*/
@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /*configura as solicitações de acesso HTTP*/
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /*ativando a proteção contra usuário que não estão validados por TOKEN*/
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        /*ativando a permissão de endpoint para utilização sem validação*/
        .disable().authorizeRequests()
        .antMatchers("/").permitAll()
        .antMatchers("/index").permitAll()
        /*URL DE Logout - redireciona após user deslogar do sistema*/
        .anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")
        /*Mapeia a URL de Logout e invalida o usuário*/
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        /*Filtra as requisições de login para autenticação*/
        .and().addFilterBefore(new JWTLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
        /*filtra demais requisições para verificar a presença do Token JWT no Header HTTP*/
        .addFilterBefore(new JWTApiAuthenticatorFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /*Service que irá consulta o usuário no banco de dados*/
        auth.userDetailsService(userDetailsService)
                /*Padrão de codificação de senha*/
        .passwordEncoder(new BCryptPasswordEncoder());

    }
}
