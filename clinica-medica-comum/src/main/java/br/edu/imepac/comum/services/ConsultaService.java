package br.edu.imepac.comum.services;

import br.edu.imepac.comum.dtos.consulta.ConsultaDto;
import br.edu.imepac.comum.dtos.consulta.ConsultaRequest;
import br.edu.imepac.comum.exceptions.NotFoundClinicaMedicaException;
import br.edu.imepac.comum.models.Consulta;
import br.edu.imepac.comum.models.Convenio;
import br.edu.imepac.comum.models.Funcionario;
import br.edu.imepac.comum.models.Paciente;
import br.edu.imepac.comum.repositories.ConsultaRepository;
import br.edu.imepac.comum.repositories.ConvenioRepository;
import br.edu.imepac.comum.repositories.FuncionarioRepository;
import br.edu.imepac.comum.repositories.PacienteRepository;
import br.edu.imepac.comum.domain.EnumTipoFuncionario;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ConsultaService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private ConvenioRepository convenioRepository;

    @Autowired
    private PerfilService perfilService;

    public ConsultaDto adicionarConsulta(ConsultaRequest request) {
        log.info("Iniciando adição de nova consulta para paciente ID: {}", request.getPacienteId());

        // Verificar se o atendente tem permissão para cadastrar consultas
        if (request.getAtendenteUsuario() == null || request.getAtendenteSenha() == null) {
            throw new IllegalArgumentException("Credenciais do atendente são obrigatórias para cadastrar consultas.");
        }
        
        boolean temPermissao = perfilService.verificarPermissaoCadastrarConsulta(
            request.getAtendenteUsuario(), 
            request.getAtendenteSenha()
        );
        
        if (!temPermissao) {
            throw new SecurityException("Acesso negado. Apenas atendentes autorizados podem cadastrar consultas.");
        }

        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> {
                    log.warn("Paciente não encontrado para o ID: {}", request.getPacienteId());
                    return new NotFoundClinicaMedicaException("Paciente não encontrado com ID: " + request.getPacienteId());
                });

        Funcionario medico = funcionarioRepository.findById(request.getMedicoId())
                .orElseThrow(() -> {
                    log.warn("Médico (Funcionário) não encontrado para o ID: {}", request.getMedicoId());
                    return new NotFoundClinicaMedicaException("Médico (Funcionário) não encontrado com ID: " + request.getMedicoId());
                });

        if (medico.getTipoFuncionario() != EnumTipoFuncionario.MEDICO) {
            log.warn("Funcionário com ID {} não é do tipo MÉDICO.", request.getMedicoId());
            throw new IllegalArgumentException("O funcionário com ID " + request.getMedicoId() + " não é um médico.");
        }

        Convenio convenio = null;
        if (request.getConvenioId() != null) {
            convenio = convenioRepository.findById(request.getConvenioId())
                    .orElseThrow(() -> {
                        log.warn("Convênio não encontrado para o ID: {}", request.getConvenioId());
                        return new NotFoundClinicaMedicaException("Convênio não encontrado com ID: " + request.getConvenioId());
                    });
        }

        Consulta consulta = modelMapper.map(request, Consulta.class);
        consulta.setPaciente(paciente);
        consulta.setMedico(medico);
        consulta.setConvenio(convenio);

        consulta.setERetorno(request.getERetorno() != null ? request.getERetorno() : false);
        consulta.setEstaAtiva(request.getEstaAtiva() != null ? request.getEstaAtiva() : true);

        Consulta savedConsulta = consultaRepository.save(consulta);
        log.info("Consulta para paciente ID {} adicionada com sucesso. ID da Consulta: {}", savedConsulta.getPaciente().getId(), savedConsulta.getId());

        return modelMapper.map(savedConsulta, ConsultaDto.class);
    }

    public List<ConsultaDto> listarConsultas() {
        log.info("Listando todas as consultas.");
        List<Consulta> consultas = consultaRepository.findAll();
        return consultas.stream()
                .map(consulta -> modelMapper.map(consulta, ConsultaDto.class))
                .collect(Collectors.toList());
    }

    public ConsultaDto buscarConsultaPorId(Long id) {
        log.info("Buscando consulta pelo ID: {}", id);
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Consulta não encontrada para o ID: {}", id);
                    return new NotFoundClinicaMedicaException("Consulta não encontrada com ID: " + id);
                });
        log.info("Consulta ID {} encontrada.", id);
        return modelMapper.map(consulta, ConsultaDto.class);
    }

    public ConsultaDto atualizarConsulta(Long id, ConsultaRequest request) {
        log.info("Iniciando atualização da consulta com ID: {}", id);

        // Verificar se o atendente tem permissão para atualizar consultas
        if (request.getAtendenteUsuario() == null || request.getAtendenteSenha() == null) {
            throw new IllegalArgumentException("Credenciais do atendente são obrigatórias para atualizar consultas.");
        }
        
        boolean temPermissao = perfilService.verificarPermissaoAtualizarConsulta(
            request.getAtendenteUsuario(), 
            request.getAtendenteSenha()
        );
        
        if (!temPermissao) {
            throw new SecurityException("Acesso negado. Apenas atendentes autorizados podem atualizar consultas.");
        }

        Consulta consultaExistente = consultaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Consulta não encontrada para atualização com ID: {}", id);
                    return new NotFoundClinicaMedicaException("Consulta não encontrada com ID: " + id);
                });

        if (request.getDataHorario() != null) {
            consultaExistente.setDataHorario(request.getDataHorario());
        }
        if (request.getSintomas() != null) {
            consultaExistente.setSintomas(request.getSintomas());
        }

        if (request.getERetorno() != null) {
            consultaExistente.setERetorno(request.getERetorno());
        }

        if (request.getEstaAtiva() != null) {
            consultaExistente.setEstaAtiva(request.getEstaAtiva());
        }

        if (request.getPacienteId() != null) {
            Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                    .orElseThrow(() -> {
                        log.warn("Paciente não encontrado para o ID: {}", request.getPacienteId());
                        return new NotFoundClinicaMedicaException("Paciente não encontrado com ID: " + request.getPacienteId());
                    });
            consultaExistente.setPaciente(paciente);
        }

        if (request.getMedicoId() != null) {
            Funcionario medico = funcionarioRepository.findById(request.getMedicoId())
                    .orElseThrow(() -> {
                        log.warn("Médico (Funcionário) não encontrado para o ID: {}", request.getMedicoId());
                        return new NotFoundClinicaMedicaException("Médico (Funcionário) não encontrado com ID: " + request.getMedicoId());
                    });
            if (medico.getTipoFuncionario() != EnumTipoFuncionario.MEDICO) {
                log.warn("Funcionário com ID {} não é do tipo MÉDICO.", request.getMedicoId());
                throw new IllegalArgumentException("O funcionário com ID " + request.getMedicoId() + " não é um médico.");
            }
            consultaExistente.setMedico(medico);
        }

        if (request.getConvenioId() != null) {
            Convenio convenio = convenioRepository.findById(request.getConvenioId())
                    .orElseThrow(() -> {
                        log.warn("Convênio não encontrado para o ID: {}", request.getConvenioId());
                        return new NotFoundClinicaMedicaException("Convênio não encontrado com ID: " + request.getConvenioId());
                    });
            consultaExistente.setConvenio(convenio);
        } else {
            consultaExistente.setConvenio(null);
        }

        Consulta updatedConsulta = consultaRepository.save(consultaExistente);
        log.info("Consulta ID {} atualizada com sucesso.", id);
        return modelMapper.map(updatedConsulta, ConsultaDto.class);
    }

    public void removerConsulta(Long id, String atendenteUsuario, Integer atendenteSenha) {
        log.info("Iniciando remoção da consulta com ID: {}", id);
        
        // Verificar se o atendente tem permissão para deletar consultas
        if (atendenteUsuario == null || atendenteSenha == null) {
            throw new IllegalArgumentException("Credenciais do atendente são obrigatórias para remover consultas.");
        }
        
        boolean temPermissao = perfilService.verificarPermissaoDeletarConsulta(
            atendenteUsuario, 
            atendenteSenha
        );
        
        if (!temPermissao) {
            throw new SecurityException("Acesso negado. Apenas atendentes autorizados podem remover consultas.");
        }
        
        if (!consultaRepository.existsById(id)) {
            log.warn("Consulta não encontrada para remoção com ID: {}", id);
            throw new NotFoundClinicaMedicaException("Consulta não encontrada com ID: " + id);
        }
        consultaRepository.deleteById(id);
        log.info("Consulta com ID: {} removida com sucesso.", id);
    }
}