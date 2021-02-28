package springjwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import springjwt.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
	
}
