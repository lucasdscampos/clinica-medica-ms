package br.edu.imepac.comum.services;

import br.edu.imepac.comum.dtos.especialidade.EspecialidadeDto;
import br.edu.imepac.comum.dtos.especialidade.EspecialidadeRequest;
import br.edu.imepac.comum.models.Especialidade;
import br.edu.imepac.comum.repositories.EspecialidadeRepository;
import br.edu.imepac.comum.exceptions.NotFoundClinicaMedicaException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class EspecialidadeService {

    private final ModelMapper modelMapper;
    private final EspecialidadeRepository especialidadeRepository;
    
    @Autowired
    private PerfilService perfilService;

    public EspecialidadeService(ModelMapper modelMapper, EspecialidadeRepository especialidadeRepository) {
        this.modelMapper = modelMapper;
        this.especialidadeRepository = especialidadeRepository;
    }

    public EspecialidadeDto adicionarEspecialidade(EspecialidadeRequest especialidadeRequest) {
        log.info("Cadastro de especialidade - service: {}", especialidadeRequest);
        
        // Verificar se o administrador tem permissão para cadastrar especialidades
        if (especialidadeRequest.getAdminUsuario() == null || especialidadeRequest.getAdminSenha() == null) {
            throw new IllegalArgumentException("Credenciais do administrador são obrigatórias para cadastrar especialidades.");
        }
        
        boolean temPermissao = perfilService.verificarPermissaoCadastrarEspecialidade(
            especialidadeRequest.getAdminUsuario(), 
            especialidadeRequest.getAdminSenha()
        );
        
        if (!temPermissao) {
            throw new SecurityException("Acesso negado. Apenas administradores podem cadastrar especialidades.");
        }
        
        Especialidade especialidade = modelMapper.map(especialidadeRequest, Especialidade.class);
        especialidade = especialidadeRepository.save(especialidade);
        return modelMapper.map(especialidade, EspecialidadeDto.class);
    }

    public EspecialidadeDto atualizarEspecialidade(Long id, EspecialidadeDto especialidadeDto) {
        log.info("Atualizando especialidade com ID: {}", id);
        Especialidade especialidadeExistente = especialidadeRepository.findById(id)
                .orElseThrow(() -> new NotFoundClinicaMedicaException("Especialidade não encontrada com ID: " + id));

        especialidadeExistente.setNome(especialidadeDto.getNome());

        Especialidade especialidadeAtualizada = especialidadeRepository.save(especialidadeExistente);
        return modelMapper.map(especialidadeAtualizada, EspecialidadeDto.class);
    }

    public EspecialidadeDto atualizarEspecialidade(Long id, EspecialidadeRequest especialidadeRequest) {
        log.info("Atualizando especialidade com ID: {}", id);
        
        // Verificar se o administrador tem permissão para atualizar especialidades
        if (especialidadeRequest.getAdminUsuario() == null || especialidadeRequest.getAdminSenha() == null) {
            throw new IllegalArgumentException("Credenciais do administrador são obrigatórias para atualizar especialidades.");
        }
        
        boolean temPermissao = perfilService.verificarPermissaoAtualizarEspecialidade(
            especialidadeRequest.getAdminUsuario(), 
            especialidadeRequest.getAdminSenha()
        );
        
        if (!temPermissao) {
            throw new SecurityException("Acesso negado. Apenas administradores podem atualizar especialidades.");
        }

        Especialidade especialidadeExistente = especialidadeRepository.findById(id)
                .orElseThrow(() -> new NotFoundClinicaMedicaException("Especialidade não encontrada com ID: " + id));

        especialidadeExistente.setNome(especialidadeRequest.getNome());
        especialidadeExistente.setDescricao(especialidadeRequest.getDescricao());

        Especialidade especialidadeAtualizada = especialidadeRepository.save(especialidadeExistente);
        return modelMapper.map(especialidadeAtualizada, EspecialidadeDto.class);
    }

    public void removerEspecialidade(Long id) {
        log.info("Removendo especialidade com ID: {}", id);
        Especialidade especialidade = especialidadeRepository.findById(id)
                .orElseThrow(() -> new NotFoundClinicaMedicaException("Especialidade não encontrada com ID: " + id));
        especialidadeRepository.delete(especialidade);
    }

    public void removerEspecialidade(Long id, String adminUsuario, Integer adminSenha) {
        log.info("Removendo especialidade com ID: {}", id);
        
        // Verificar se o administrador tem permissão para deletar especialidades
        if (adminUsuario == null || adminSenha == null) {
            throw new IllegalArgumentException("Credenciais do administrador são obrigatórias para remover especialidades.");
        }
        
        boolean temPermissao = perfilService.verificarPermissaoDeletarEspecialidade(adminUsuario, adminSenha);
        
        if (!temPermissao) {
            throw new SecurityException("Acesso negado. Apenas administradores podem remover especialidades.");
        }
        
        Especialidade especialidade = especialidadeRepository.findById(id)
                .orElseThrow(() -> new NotFoundClinicaMedicaException("Especialidade não encontrada com ID: " + id));
        especialidadeRepository.delete(especialidade);
    }

    public EspecialidadeDto buscarEspecialidadePorId(Long id) {
        log.info("Buscando especialidade com ID: {}", id);
        Especialidade especialidade = especialidadeRepository.findById(id)
                .orElseThrow(() -> new NotFoundClinicaMedicaException("Especialidade não encontrada com ID: " + id));
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