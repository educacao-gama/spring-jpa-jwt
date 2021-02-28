package springjwt;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import springjwt.model.Usuario;
import springjwt.repository.UsuarioRepository;

@SpringBootApplication
public class SpringJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringJwtApplication.class, args);
	}
	@Bean
    public CommandLineRunner run(UsuarioRepository r, PasswordEncoder encoder) throws Exception {
        return args -> {
        	Usuario u = new Usuario();
        	u.setLogin("admin");
        	String senhaCriptografada = encoder.encode("admin123");
        	u.setSenha(senhaCriptografada);
        	r.save(u);
        };
	}
}
