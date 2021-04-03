package curso.api.rest.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import curso.api.rest.model.Usuario;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*Estabelece o gerenciador de token*/
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    /*configura o gerenciador de autenticação*/
    protected JWTLoginFilter(String url, AuthenticationManager authenticationManager) {

        /*obriga a autenticar a aurl*/
        super(new AntPathRequestMatcher(url));

        /*Gerenciado de autenticação*/
        setAuthenticationManager(authenticationManager);
    }

    /*retorna o usuário ao preocessa a autenticação*/
    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {

        /*pega o token do usuário pra validação*/
        Usuario user = new ObjectMapper().readValue(httpServletRequest.getInputStream(), Usuario.class);

        /*retorna o usuário login, senha e acessos*/
        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        new JWTTokenAuthenticatorService().addAuthentication(response, authResult.getName());
    }
}
