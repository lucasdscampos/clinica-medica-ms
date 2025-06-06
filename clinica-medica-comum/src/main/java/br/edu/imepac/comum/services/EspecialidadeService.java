package br.edu.imepac.comum.services;

import br.edu.imepac.comum.dtos.especialidade.EspecialidadeDto;
import br.edu.imepac.comum.dtos.especialidade.EspecialidadeRequest;
import br.edu.imepac.comum.models.Especialidade;
import br.edu.imepac.comum.repositories.EspecialidadeRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class EspecialidadeService {

    private ModelMapper modelMapper;

    private EspecialidadeRepository especialidadeRepository;


    public EspecialidadeService(ModelMapper modelMapper, EspecialidadeRepository especialidadeRepository) {
        this.modelMapper = modelMapper;
        this.especialidadeRepository = especialidadeRepository;
    }

    // Implementação de métodos para manipulação de especialidades
    public EspecialidadeDto adicionarEspecialidade(EspecialidadeRequest especialidadeRequest) {
        log.info("Cadadastro de especialidade - service: {}", especialidadeRequest);
        Especialidade especialidade = modelMapper.map(especialidadeRequest, Especialidade.class);
        especialidade = especialidadeRepository.save(especialidade);
        return modelMapper.map(especialidade, EspecialidadeDto.class); // Retornar o dto criado
    }

    public EspecialidadeDto atualizarEspecialidade(Long id, EspecialidadeDto especialidadeDto) {
        log.info("Atualizando especialidade com ID: {}", id);
        Especialidade especialidadeExistente = especialidadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidade não encontrada com ID: " + id));
        modelMapper.map(especialidadeDto, especialidadeExistente);
        Especialidade especialidadeAtualizada = especialidadeRepository.save(especialidadeExistente);
        return modelMapper.map(especialidadeAtualizada, EspecialidadeDto.class);
    }

    public void removerEspecialidade(Long id) {
        log.info("Removendo especialidade com ID: {}", id);
        Especialidade especialidade = especialidadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidade não encontrada com ID: " + id));
        especialidadeRepository.delete(especialidade);
    }

    public EspecialidadeDto buscarEspecialidadePorId(Long id) {
        log.info("Buscando especialidade com ID: {}", id);
        Especialidade especialidade = especialidadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidade não encontrada com ID: " + id));
        return modelMapper.map(especialidade, EspecialidadeDto.class);
    }

    public List<EspecialidadeDto> listarEspecialidades() {
        log.info("Listando todas as especialidades");
        List<Especialidade> especialidades = especialidadeRepository.findAll();
        return especialidades.stream()
                .map(especialidade -> modelMapper.map(especialidade, EspecialidadeDto.class))
                .toList();
    }
}
