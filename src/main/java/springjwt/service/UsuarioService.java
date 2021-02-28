package springjwt.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import springjwt.model.Login;
import springjwt.model.Sessao;
import springjwt.model.Usuario;
import springjwt.repository.UsuarioRepository;
import springjwt.security.JWTConstants;

@Service
public class UsuarioService {
	//https://mkyong.com/spring-boot/spring-rest-error-handling-example/
	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private PasswordEncoder encoder;
	public Sessao autenticar( Login login ) {
		if (login == null || login.getUsuario()==null || login.getSenha()==null) {
			throw new RuntimeException("Login e senha são requeridos");
		}

		Optional<Usuario> optuser = repository.findById(login.getUsuario());
		Usuario usuario = null;
		if(optuser.isPresent()) {
			usuario = optuser.get();
		}else {
			throw new RuntimeException(login.getUsuario());
		}
			
		boolean senhaOk = encoder.matches(login.getSenha(),usuario.getSenha());

		if (!senhaOk) {
			throw new RuntimeException("Senha inválida para o login: " + login.getUsuario());
		}

		Sessao sessao = new Sessao();
		sessao.setDataInicio(new Date(System.currentTimeMillis()));
		sessao.setDataFim(new Date(System.currentTimeMillis() + JWTConstants.TOKEN_EXPIRATION));
		
		sessao.setLogin(usuario.getLogin());

		sessao.setToken(JWTConstants.PREFIX + getJWTToken(sessao));

		return sessao;
	}
	
	//como vc gerenciaria a nivel de banco o role de um usuario ???
	private String getJWTToken(Sessao sessao) {
		String role = "ROLE_USER";
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(role);

		String token = Jwts.builder().setSubject(sessao.getLogin())
				.claim("authorities",
						grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.setIssuedAt(sessao.getDataInicio()).setExpiration(sessao.getDataFim())
				.signWith(SignatureAlgorithm.HS512, JWTConstants.KEY.getBytes()).compact();

		return token;
	}
}
