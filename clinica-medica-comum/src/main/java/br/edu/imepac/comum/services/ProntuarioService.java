package br.edu.imepac.comum.services;

import br.edu.imepac.comum.dtos.prontuario.ProntuarioDto;
import br.edu.imepac.comum.dtos.prontuario.ProntuarioRequest;
import br.edu.imepac.comum.exceptions.NotFoundClinicaMedicaException;
import br.edu.imepac.comum.models.Prontuario;
import br.edu.imepac.comum.models.Consulta;
import br.edu.imepac.comum.repositories.ProntuarioRepository;
import br.edu.imepac.comum.repositories.ConsultaRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProntuarioService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProntuarioRepository prontuarioRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    public ProntuarioDto adicionarProntuario(ProntuarioRequest request) {
        log.info("Iniciando adição de novo prontuário para consulta ID: {}", request.getConsultaId());

        Consulta consulta = consultaRepository.findById(request.getConsultaId())
                .orElseThrow(() -> {
                    log.warn("Consulta não encontrada para o ID: {}", request.getConsultaId());
                    return new NotFoundClinicaMedicaException("Consulta não encontrada com ID: " + request.getConsultaId());
                });

        if (prontuarioRepository.findByConsulta(consulta).isPresent()) {
            log.warn("Já existe um prontuário associado à consulta com ID: {}", request.getConsultaId());
            throw new IllegalArgumentException("Já existe um prontuário para a consulta com ID: " + request.getConsultaId());
        }

        Prontuario prontuario = modelMapper.map(request, Prontuario.class);
        prontuario.setConsulta(consulta);

        Prontuario savedProntuario = prontuarioRepository.save(prontuario);
        log.info("Prontuário ID: {} criado com sucesso para consulta ID: {}", savedProntuario.getId(), savedProntuario.getConsulta().getId());

        return modelMapper.map(savedProntuario, ProntuarioDto.class);
    }

    public List<ProntuarioDto> listarProntuarios() {
        log.info("Listando todos os prontuários.");
        List<Prontuario> prontuarios = prontuarioRepository.findAll();
        return prontuarios.stream()
                .map(prontuario -> modelMapper.map(prontuario, ProntuarioDto.class))
                .collect(Collectors.toList());
    }

    public ProntuarioDto buscarProntuarioPorId(Long id) {
        log.info("Buscando prontuário pelo ID: {}", id);
        Prontuario prontuario = prontuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Prontuário não encontrado para o ID: {}", id);
                    return new NotFoundClinicaMedicaException("Prontuário não encontrado com ID: " + id);
                });
        log.info("Prontuário ID: {} encontrado.", prontuario.getId());
        return modelMapper.map(prontuario, ProntuarioDto.class);
    }

    public ProntuarioDto atualizarProntuario(Long id, ProntuarioRequest request) {
        log.info("Iniciando atualização do prontuário com ID: {}", id);

        Prontuario prontuarioExistente = prontuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Prontuário não encontrado para atualização com ID: {}", id);
                    return new NotFoundClinicaMedicaException("Prontuário não encontrado com ID: " + id);
                });

        if (!prontuarioExistente.getConsulta().getId().equals(request.getConsultaId())) {
            Consulta novaConsulta = consultaRepository.findById(request.getConsultaId())
                    .orElseThrow(() -> {
                        log.warn("Nova Consulta não encontrada para o ID: {}", request.getConsultaId());
                        return new NotFoundClinicaMedicaException("Nova Consulta não encontrada com ID: " + request.getConsultaId());
                    });

            if (prontuarioRepository.findByConsulta(novaConsulta).isPresent()) {
                log.warn("A nova consulta com ID: {} já possui um prontuário associado.", request.getConsultaId());
                throw new IllegalArgumentException("A consulta com ID: " + request.getConsultaId() + " já possui um prontuário associado.");
            }
            prontuarioExistente.setConsulta(novaConsulta);
        }

        if (request.getReceituario() != null) {
            prontuarioExistente.setReceituario(request.getReceituario());
        }
        if (request.getExames() != null) {
            prontuarioExistente.setExames(request.getExames());
        }
        if (request.getObservacoes() != null) {
            prontuarioExistente.setObservacoes(request.getObservacoes());
        }

        Prontuario updatedProntuario = prontuarioRepository.save(prontuarioExistente);
        log.info("Prontuário ID: {} atualizado com sucesso.", id);
        return modelMapper.map(updatedProntuario, ProntuarioDto.class);
    }

    public void removerProntuario(Long id) {
        log.info("Iniciando remoção do prontuário com ID: {}", id);
        if (!prontuarioRepository.existsById(id)) {
            log.warn("Prontuário não encontrado para remoção com ID: {}", id);
            throw new NotFoundClinicaMedicaException("Prontuário não encontrado com ID: " + id);
        }
        prontuarioRepository.deleteById(id);
        log.info("Prontuário com ID: {} removido com sucesso.", id);
    }
}