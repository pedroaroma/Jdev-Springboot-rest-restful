package curso.api.rest.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class geraSenha {
    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("pedro"));
    }
}
