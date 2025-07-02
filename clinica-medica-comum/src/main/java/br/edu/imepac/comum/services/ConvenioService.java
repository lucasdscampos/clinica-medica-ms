package br.edu.imepac.comum.services;

import br.edu.imepac.comum.dtos.convenio.ConvenioDto;
import br.edu.imepac.comum.dtos.convenio.ConvenioRequest;
import br.edu.imepac.comum.models.Convenio;
import br.edu.imepac.comum.repositories.ConvenioRepository;
import br.edu.imepac.comum.exceptions.NotFoundClinicaMedicaException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ConvenioService {

    private final ModelMapper modelMapper;
    private final ConvenioRepository convenioRepository;
    
    @Autowired
    private PerfilService perfilService;

    public ConvenioService(ModelMapper modelMapper, ConvenioRepository convenioRepository) {
        this.modelMapper = modelMapper;
        this.convenioRepository = convenioRepository;
    }

    public ConvenioDto adicionarConvenio(ConvenioRequest convenioRequest) {
        log.info("Adicionando novo convênio com nome: {}", convenioRequest.getNome());
        
        // Verificar se o administrador tem permissão para cadastrar convênios
        if (convenioRequest.getAdminUsuario() == null || convenioRequest.getAdminSenha() == null) {
            throw new IllegalArgumentException("Credenciais do administrador são obrigatórias para cadastrar convênios.");
        }
        
        boolean temPermissao = perfilService.verificarPermissaoCadastrarConvenio(
            convenioRequest.getAdminUsuario(), 
            convenioRequest.getAdminSenha()
        );
        
        if (!temPermissao) {
            throw new SecurityException("Acesso negado. Apenas administradores podem cadastrar convênios.");
        }
        
        Convenio convenio = modelMapper.map(convenioRequest, Convenio.class);
        Convenio convenioSalvo = convenioRepository.save(convenio);
        return modelMapper.map(convenioSalvo, ConvenioDto.class);
    }

    public List<ConvenioDto> listarConvenios() {
        log.info("Listando todos os convênios.");
        List<Convenio> convenios = convenioRepository.findAll();
        return convenios.stream()
                .map(convenio -> modelMapper.map(convenio, ConvenioDto.class))
                .collect(Collectors.toList());
    }

    public ConvenioDto buscarConvenioPorId(Long id) {
        log.info("Buscando convênio com ID: {}", id);
        Convenio convenio = convenioRepository.findById(id)
                .orElseThrow(() -> new NotFoundClinicaMedicaException("Convênio não encontrado com ID: " + id));
        return modelMapper.map(convenio, ConvenioDto.class);
    }

    public ConvenioDto atualizarConvenio(Long id, ConvenioDto convenioDto) {
        log.info("Atualizando convênio com ID: {}", id);

        Convenio convenioExistente = convenioRepository.findById(id)
                .orElseThrow(() -> new NotFoundClinicaMedicaException("Convênio não encontrado com ID: " + id));

        convenioExistente.setNome(convenioDto.getNome());
        convenioExistente.setDescricao(convenioDto.getDescricao());

        Convenio convenioAtualizado = convenioRepository.save(convenioExistente);
        return modelMapper.map(convenioAtualizado, ConvenioDto.class);
    }

    public ConvenioDto atualizarConvenio(Long id, ConvenioRequest convenioRequest) {
        log.info("Atualizando convênio com ID: {}", id);
        
        // Verificar se o administrador tem permissão para atualizar convênios
        if (convenioRequest.getAdminUsuario() == null || convenioRequest.getAdminSenha() == null) {
            throw new IllegalArgumentException("Credenciais do administrador são obrigatórias para atualizar convênios.");
        }
        
        boolean temPermissao = perfilService.verificarPermissaoAtualizarConvenio(
            convenioRequest.getAdminUsuario(), 
            convenioRequest.getAdminSenha()
        );
        
        if (!temPermissao) {
            throw new SecurityException("Acesso negado. Apenas administradores podem atualizar convênios.");
        }

        Convenio convenioExistente = convenioRepository.findById(id)
                .orElseThrow(() -> new NotFoundClinicaMedicaException("Convênio não encontrado com ID: " + id));

        convenioExistente.setNome(convenioRequest.getNome());
        convenioExistente.setDescricao(convenioRequest.getDescricao());

        Convenio convenioAtualizado = convenioRepository.save(convenioExistente);
        return modelMapper.map(convenioAtualizado, ConvenioDto.class);
    }

    public void removerConvenio(Long id) {
        log.info("Removendo convênio com ID: {}", id);
        Convenio convenio = convenioRepository.findById(id)
                .orElseThrow(() -> new NotFoundClinicaMedicaException("Convênio não encontrado com ID: " + id));
        convenioRepository.delete(convenio);
    }

    public void removerConvenio(Long id, String adminUsuario, Integer adminSenha) {
        log.info("Removendo convênio com ID: {}", id);
        
        // Verificar se o administrador tem permissão para deletar convênios
        if (adminUsuario == null || adminSenha == null) {
            throw new IllegalArgumentException("Credenciais do administrador são obrigatórias para remover convênios.");
        }
        
        boolean temPermissao = perfilService.verificarPermissaoDeletarConvenio(adminUsuario, adminSenha);
        
        if (!temPermissao) {
            throw new SecurityException("Acesso negado. Apenas administradores podem remover convênios.");
        }
        
        Convenio convenio = convenioRepository.findById(id)
                .orElseThrow(() -> new NotFoundClinicaMedicaException("Convênio não encontrado com ID: " + id));
        convenioRepository.delete(convenio);
    }
}