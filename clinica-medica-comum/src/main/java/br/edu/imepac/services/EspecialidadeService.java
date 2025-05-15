package br.edu.imepac.services;

import br.edu.imepac.dtos.especialidade.EspecialidadeDto;
import br.edu.imepac.dtos.especialidade.EspecialidadeRequest;
import br.edu.imepac.repositories.EspecialidadeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspecialidadeService {
    private final EspecialidadeRepository especialidadeRepository;

    public EspecialidadeService(EspecialidadeRepository especialidadeRepository) {
        this.especialidadeRepository = especialidadeRepository;
    }

    // Implementação de métodos para manipulação de especialidades
    public void adicionarEspecialidade(EspecialidadeRequest especialidadeRequest) {
        // Lógica para adicionar uma nova especialidade
        //validar os dados do request
        //converter o request em um objeto de domínio
        //salvar o objeto no repositório
    }

    public void atualizarEspecialidade(Long id, EspecialidadeDto especialidadeDto) {
        // Lógica para atualizar uma especialidade existente
        //validar os dados do dto
        //converter o dto em um objeto de domínio
        //atualizar o objeto no repositório
    }

    public void removerEspecialidade(Long id) {
        // Lógica para remover uma especialidade
        //validar o id
        //remover o objeto no repositório
    }

    public EspecialidadeDto buscarEspecialidadePorId(Long id) {
        // Lógica para buscar uma especialidade por ID
        //validar o id
        //buscar o objeto no repositório
        //converter o objeto em um dto
        return null; // Retornar o dto encontrado
    }

    public List<EspecialidadeDto> listarEspecialidades() {
        // Lógica para listar todas as especialidades
        //buscar todos os objetos no repositório
        //converter os objetos em dtos
        return null; // Retornar a lista de dtos encontrados
    }

}
