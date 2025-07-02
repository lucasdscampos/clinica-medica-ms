package br.edu.imepac.comum.repositories;

import br.edu.imepac.comum.models.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    
    List<Funcionario> findByUsuarioAndSenha(String usuario, Integer senha);
}