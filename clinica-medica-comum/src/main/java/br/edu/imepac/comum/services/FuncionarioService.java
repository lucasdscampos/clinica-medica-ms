package br.edu.imepac.comum.services;

import br.edu.imepac.comum.dtos.funcionario.FuncionarioDto;
import br.edu.imepac.comum.dtos.funcionario.FuncionarioRequest;
import br.edu.imepac.comum.exceptions.NotFoundClinicaMedicaException;
import br.edu.imepac.comum.models.Funcionario;
import br.edu.imepac.comum.models.Perfil;
import br.edu.imepac.comum.models.Especialidade;
import br.edu.imepac.comum.repositories.FuncionarioRepository;
import br.edu.imepac.comum.repositories.PerfilRepository;
import br.edu.imepac.comum.repositories.EspecialidadeRepository;
import br.edu.imepac.comum.domain.EnumTipoFuncionario;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FuncionarioService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private EspecialidadeRepository especialidadeRepository;

    @Autowired
    private PerfilService perfilService;

    public FuncionarioDto adicionarFuncionario(FuncionarioRequest request) {
        log.info("Adicionando novo funcionário: {}", request.getNome());

        // Verificar se o administrador tem permissão para cadastrar funcionários
        if (request.getAdminUsuario() == null || request.getAdminSenha() == null) {
            throw new IllegalArgumentException("Credenciais do administrador são obrigatórias para cadastrar funcionários.");
        }

        boolean temPermissao = perfilService.verificarPermissaoCadastrarFuncionario(
                request.getAdminUsuario(),
                request.getAdminSenha()
        );

        if (!temPermissao) {
            throw new SecurityException("Acesso negado. Apenas administradores podem cadastrar funcionários.");
        }

        // --- LINHAS DE LOG PARA DEPURAR (ADICIONADAS AQUI) ---
        log.info("Valor de request.getIdPerfil() antes da busca: {}", request.getIdPerfil());
        log.info("Valor de request.getIdEspecialidade() antes da busca: {}", request.getIdEspecialidade());
        // --- FIM DAS LINHAS DE LOG ---

        Perfil perfil = perfilRepository.findById(request.getIdPerfil())
                .orElseThrow(() -> new NotFoundClinicaMedicaException("Perfil não encontrado com o ID: " + request.getIdPerfil()));

        Funcionario funcionario = modelMapper.map(request, Funcionario.class);
        funcionario.setPerfil(perfil);

        handleMedicoSpecificFields(request, funcionario);

        Funcionario savedFuncionario = funcionarioRepository.save(funcionario);
        log.info("Funcionário {} adicionado com sucesso.", savedFuncionario.getNome());

        return modelMapper.map(savedFuncionario, FuncionarioDto.class);
    }

    public List<FuncionarioDto> listarFuncionarios() {
        log.info("Listando todos os funcionários.");
        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        return funcionarios.stream()
                .map(funcionario -> modelMapper.map(funcionario, FuncionarioDto.class))
                .collect(Collectors.toList());
    }

    public FuncionarioDto buscarFuncionarioPorId(Long id) {
        log.info("Buscando funcionário pelo ID: {}", id);
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundClinicaMedicaException("Funcionário não encontrado com o ID: " + id));
        log.info("Funcionário {} encontrado.", funcionario.getNome());
        return modelMapper.map(funcionario, FuncionarioDto.class);
    }

    public FuncionarioDto atualizarFuncionario(Long id, FuncionarioRequest request) {
        log.info("Atualizando funcionário com ID: {}", id);
        Funcionario funcionarioExistente = funcionarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundClinicaMedicaException("Funcionário não encontrado com o ID: " + id));

        if (request.getIdPerfil() != null) {
            Perfil novoPerfil = perfilRepository.findById(request.getIdPerfil())
                    .orElseThrow(() -> new NotFoundClinicaMedicaException("Perfil não encontrado com o ID: " + request.getIdPerfil()));
            funcionarioExistente.setPerfil(novoPerfil);
        }

        modelMapper.map(request, funcionarioExistente);

        handleMedicoSpecificFields(request, funcionarioExistente);

        Funcionario updatedFuncionario = funcionarioRepository.save(funcionarioExistente);
        log.info("Funcionário {} (ID: {}) atualizado com sucesso.", updatedFuncionario.getNome(), id);

        return modelMapper.map(updatedFuncionario, FuncionarioDto.class);
    }

    public void removerFuncionario(Long id) {
        log.info("Removendo funcionário com ID: {}", id);
        if (!funcionarioRepository.existsById(id)) {
            throw new NotFoundClinicaMedicaException("Funcionário não encontrado com o ID: " + id);
        }
        funcionarioRepository.deleteById(id);
        log.info("Funcionário com ID: {} removido com sucesso.", id);
    }

    private void handleMedicoSpecificFields(FuncionarioRequest request, Funcionario funcionario) {
        EnumTipoFuncionario tipoFuncionarioAtualizado;

        if (request.getTipoFuncionario() != null) {
            tipoFuncionarioAtualizado = EnumTipoFuncionario.valueOf(request.getTipoFuncionario().toUpperCase());
        } else {
            tipoFuncionarioAtualizado = funcionario.getTipoFuncionario();
        }

        funcionario.setTipoFuncionario(tipoFuncionarioAtualizado);

        if (tipoFuncionarioAtualizado == EnumTipoFuncionario.MEDICO) {
            if (request.getIdEspecialidade() == null) {
                throw new IllegalArgumentException("Especialidade é obrigatória para funcionários do tipo MÉDICO.");
            }
            Especialidade especialidade = especialidadeRepository.findById(request.getIdEspecialidade())
                    .orElseThrow(() -> new NotFoundClinicaMedicaException("Especialidade não encontrada com o ID: " + request.getIdEspecialidade()));
            funcionario.setEspecialidade(especialidade);
        } else {
            funcionario.setEspecialidade(null);
        }
    }
}