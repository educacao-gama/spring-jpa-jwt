package springjwt.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import springjwt.model.Login;
import springjwt.model.Sessao;
import springjwt.service.UsuarioService;

@RestController
@RequestMapping("/login")
public class LoginResource {
	@Autowired
	private UsuarioService service;
	
	@PostMapping
	public Sessao logar(@RequestBody Login login) throws Exception {
		return service.autenticar(login);
	}
	
}
