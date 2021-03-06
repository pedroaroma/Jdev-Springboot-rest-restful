package curso.api.rest.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/*Filtro onde todas as requisições serão capturadas para autenticar*/
public class JWTApiAuthenticatorFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        /*Estabelece autenticação para a requisição*/
        Authentication authentication = new JWTTokenAuthenticatorService().getAuthentication((HttpServletRequest) servletRequest);

        /*Coloca o processo de autenticação no spring security*/
        SecurityContextHolder.getContext().setAuthentication(authentication);

        /*Continua o processo*/
        filterChain.doFilter(servletRequest, servletResponse);


    }
}
