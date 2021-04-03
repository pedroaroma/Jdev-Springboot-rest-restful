package curso.api.rest.security;

import curso.api.rest.ApplicationContextLoad;
import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Service
@Component
public class JWTTokenAuthenticatorService {

    /*Validade do Token em Milisegundos*/
    private static final long EXPIRATION_TIME = 172800000;
    /*senha unica para compor a autenticação, seria encessário?*/
    private static final String SECRET = "$%EJFGHe56yert3eWYtR#kj$%";
    /*prefixo d token*/
    private static final String TOKEN_PREFIX = "Bearer";

    private static final String HEADER_STRING = "Authorization";

    /*Gerando token de autenticação e adicionando ao cabeçalho e resposta http*/
    public void addAuthentication(HttpServletResponse response, String username) throws IOException {

        /*Montagem do Token*/
        String JWT = Jwts.builder() /*invoca o gerador de token*/
                .setSubject(username) /*Adiciona o usuário*/
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) /*define o tempo de expiração*/
                .signWith(SignatureAlgorithm.HS256, SECRET).compact(); /*define o algoritmo de criptografia para geração do token*/

        String token = TOKEN_PREFIX + " " + JWT; //Bearer {token}

        /*adiciona ao cabeçalho http*/
        response.addHeader(HEADER_STRING, token); //Authorization: Bearer {token}

        /*Escreve token como resposta no corpo do http*/
        response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
    }

    /*Retorna o usuário validado com token ou caso não seja valido retorna null*/
    public Authentication getAuthentication(HttpServletRequest request) {

        /*Pega o token enviado no cabeçalho http*/
        String token = request.getHeader(HEADER_STRING);

        if (token != null) {

            /*faz a validação do token do usuário na requisição*/
            String user = Jwts.parser().setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody().getSubject(); /*retorna o usuário*/

            if (token != null) {
                Usuario usuario = ApplicationContextLoad.getApplicationContext().getBean(UsuarioRepository.class).findUserByLogin(user);

                /*Retornar o usuário logado*/
                if (user != null) {
                    return new UsernamePasswordAuthenticationToken(
                            usuario.getUsername(),
                            usuario.getPassword(),
                            usuario.getAuthorities());
                }
            }
        }
        return null; /*usuário não autorizado*/
    }
}
