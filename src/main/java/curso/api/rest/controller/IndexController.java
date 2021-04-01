package curso.api.rest.controller;

import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController /*Arquitetura REST*/
@RequestMapping(value = "/usuario")
public class IndexController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /*Serviço RESTFul*/
    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<List<Usuario>> getAllUsers() {

        List<Usuario> usuarios = (List<Usuario>) usuarioRepository.findAll();

        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity getUserById(@PathVariable (value = "id") Long id) {

        Optional<Usuario> usuario = usuarioRepository.findById(id);

        return new ResponseEntity(usuario.get(), HttpStatus.OK);
    }

    @PostMapping(value = "/", produces = "application/json")
    public ResponseEntity<Usuario> createNewUser(@RequestBody Usuario user) {

        Usuario usuario = usuarioRepository.save(user);

        return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
    }

    @PatchMapping(value = "/", produces = "application/json")
    public ResponseEntity<Usuario> updateUser(@RequestBody Usuario user) {

        Usuario usuario = usuarioRepository.save(user);

        return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
    }


}
