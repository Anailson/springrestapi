package curso.api.rest.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import curso.api.rest.model.Usuario;
import curso.api.rest.model.UsuarioDTO;
import curso.api.rest.repository.UsuarioRepository;

//@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/usuario")
public class IndexController {

	// CHAMANDO A CONTROLLE
	@Autowired
	private UsuarioRepository usuarioRepository;

	/* SERVIÇO RESTFULL */
	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<UsuarioDTO> init(@PathVariable(value = "id") Long id) {

		// CHAMANDO POR ID OS DADOS
		Optional<Usuario> usuario = usuarioRepository.findById(id);

		// USANDO A CLASSE DTO - OCULTA OS NOMES REAIS DO ATRIBUTOS NO BANCO
		// EXEMPLO: nome sera userNome
		return new ResponseEntity<UsuarioDTO>(new UsuarioDTO(usuario.get()), HttpStatus.OK);

	}

	/*
	 * Vamos supor que o carregamento de usuário seja um processo lento e queremos
	 * controlar ele com cache para agilizar o processo
	 */
	// LISTANDO TODOS OS ID'S
	@GetMapping(value = "/", produces = "application/json")
	@CachePut("cacheusuarios")
	public ResponseEntity<List<Usuario>> usuario() {

		List<Usuario> list = (List<Usuario>) usuarioRepository.findAll();

		return new ResponseEntity<List<Usuario>>(list, HttpStatus.OK);
	}

	// SALVANDO OS DADOS CRIADO NO POSTMAN
	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) {

		// ASSOCIANDOD O OBJETO FILHO-TELEFONE AO PAI-USUARIO
		for (int pos = 0; pos < usuario.getTelefones().size(); pos++) {
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}

		// CRIPTOGRANDO A SENHA DOS USUARIOS AO SALVAR
		String senhacriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(senhacriptografada);

		Usuario usuarioSalvo = usuarioRepository.save(usuario);

		// GRAVAR NO BANCO DE DADOS O RETORNO
		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);

	}

	// ATUALIZANDO OS DADOS CRIADOS NO POSTMAN
	@PutMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> atualizar(@RequestBody Usuario usuario) {

		// ASSOCIANDOD O OBJETO FILHO-TELEFONE AO PAI-USUARIO
		for (int pos = 0; pos < usuario.getTelefones().size(); pos++) {
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}
		// ATUALIZANDO A SENHA
		Usuario userTemporario = usuarioRepository.findUserByLogin(usuario.getLogin());

		// SENHAS DIFERENTES
		if (!userTemporario.getSenha().equals(usuario.getSenha())) {
			// CRIPTOGRANDO A SENHA DOS USUARIOS AO SALVAR - CASO SEJA DIFERENTES AS SENHAS
			String senhacriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
			usuario.setSenha(senhacriptografada);
		}

		Usuario usuarioSalvo = usuarioRepository.save(usuario);

		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
	}

	// DELETE REGISTROS - 
	@DeleteMapping(value = "/{id}", produces = "application/json")
	public String delete(@PathVariable("id") Long id) {

		usuarioRepository.deleteById(id);

		return "Registro Deletado";
	}
	
	
	//PESQUISA NOME END PONT CONSULTA POR NOME
	@GetMapping(value = "/usuarioPorNome/{nome}", produces = "application/json")
	@CachePut("cacheusuarios")
	public ResponseEntity<List<Usuario>> usuarioPorNome(@PathVariable("nome") String nome) throws InterruptedException{

		List<Usuario> list = (List<Usuario>) usuarioRepository.findUserByNome(nome);

		return new ResponseEntity<List<Usuario>>(list, HttpStatus.OK);
	}
	
}
