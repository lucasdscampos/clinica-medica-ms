package br.edu.imepac.comum.repositories;

import br.edu.imepac.comum.models.Prontuario;
import br.edu.imepac.comum.models.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProntuarioRepository extends JpaRepository<Prontuario, Long> {
    Optional<Prontuario> findByConsulta(Consulta consulta);
}