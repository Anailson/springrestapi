package curso.api.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import curso.api.rest.model.Usuario;

@RestController
@RequestMapping(value = "/usuario")
public class IndexController {

	/*SERVIÇO RESTFULL*/
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> init(){
		
		Usuario usuario = new Usuario();
		usuario.setId(11L);
		usuario.setLogin("arsantos315@gmail.com");
		usuario.setNome("Anailson Ribeiro");
		usuario.setSenha("3333");
		
		Usuario usuario2 = new Usuario();
		usuario.setId(10L);
		usuario.setLogin("teste@gmail.com");
		usuario.setNome("Teste Sistema");
		usuario.setSenha("1010");
		
		/*LISTAS PARA CHAMA OS OBJETOS*/
		List<Usuario> usuarios = new ArrayList<Usuario>();
		usuarios.add(usuario);
		usuarios.add(usuario2);
		
		return new ResponseEntity(usuarios, HttpStatus.OK);
	}
	
}
