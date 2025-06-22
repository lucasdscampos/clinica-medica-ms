package br.edu.imepac.comum.services;

import br.edu.imepac.comum.dtos.perfil.PerfilDto;
import br.edu.imepac.comum.dtos.perfil.PerfilRequest;
import br.edu.imepac.comum.exceptions.NotFoundClinicaMedicaException;
import br.edu.imepac.comum.models.Perfil;
import br.edu.imepac.comum.repositories.PerfilRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PerfilService {

    private final ModelMapper modelMapper;
    private final PerfilRepository perfilRepository;

    public PerfilService(ModelMapper modelMapper, PerfilRepository perfilRepository) {
        this.modelMapper = modelMapper;
        this.perfilRepository = perfilRepository;
    }

    public PerfilDto adicionarPerfil(PerfilRequest perfilRequest) {
        log.info("Cadastro de perfil - service: {}", perfilRequest);
        Perfil perfil = modelMapper.map(perfilRequest, Perfil.class);
        Perfil perfilSalvo = perfilRepository.save(perfil);
        return modelMapper.map(perfilSalvo, PerfilDto.class);
    }

    public PerfilDto atualizarPerfil(Long id, PerfilRequest perfilRequest) {
        log.info("Atualizando perfil com ID: {}", id);
        Perfil perfilExistente = perfilRepository.findById(id)
                .orElseThrow(() -> new NotFoundClinicaMedicaException("Perfil não encontrado com ID: " + id));

        modelMapper.map(perfilRequest, perfilExistente);

        Perfil perfilAtualizado = perfilRepository.save(perfilExistente);
        return modelMapper.map(perfilAtualizado, PerfilDto.class);
    }

    public void removerPerfil(Long id) {
        log.info("Removendo perfil com ID: {}", id);
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new NotFoundClinicaMedicaException("Perfil não encontrado com ID: " + id));
        perfilRepository.delete(perfil);
    }

    public PerfilDto buscarPerfilPorId(Long id) {
        log.info("Buscando perfil com ID: {}", id);
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new NotFoundClinicaMedicaException("Perfil não encontrado com ID: " + id));
        return modelMapper.map(perfil, PerfilDto.class);
    }

    public List<PerfilDto> listarPerfis() {
        log.info("Listando todos os perfis");
        List<Perfil> perfis = perfilRepository.findAll();
        return perfis.stream()
                .map(perfil -> modelMapper.map(perfil, PerfilDto.class))
                .collect(Collectors.toList());
    }

    public boolean verificarAutorizacao(String usuario, String senha, String acao) {
        log.info("Verificando autorização para usuário: {}, senha: {}, acao: {}", usuario, senha, acao);
        return true;
    }
}