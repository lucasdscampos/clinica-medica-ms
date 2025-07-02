package br.edu.imepac.comum.services;

import br.edu.imepac.comum.dtos.perfil.PerfilDto;
import br.edu.imepac.comum.dtos.perfil.PerfilRequest;
import br.edu.imepac.comum.exceptions.NotFoundClinicaMedicaException;
import br.edu.imepac.comum.models.Funcionario;
import br.edu.imepac.comum.models.Perfil;
import br.edu.imepac.comum.repositories.FuncionarioRepository;
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
    private final FuncionarioRepository funcionarioRepository;

    public PerfilService(ModelMapper modelMapper, PerfilRepository perfilRepository, FuncionarioRepository funcionarioRepository) {
        this.modelMapper = modelMapper;
        this.perfilRepository = perfilRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    public PerfilDto adicionarPerfil(PerfilRequest perfilRequest) {
        log.info("Cadastro de perfil - service: {}", perfilRequest);
        
        // Verificar se o administrador tem permissão para cadastrar perfis
        if (perfilRequest.getAdminUsuario() == null || perfilRequest.getAdminSenha() == null) {
            throw new IllegalArgumentException("Credenciais do administrador são obrigatórias para cadastrar perfis.");
        }
        
        boolean temPermissao = verificarPermissaoCadastrarPerfil(
            perfilRequest.getAdminUsuario(), 
            perfilRequest.getAdminSenha()
        );
        
        if (!temPermissao) {
            throw new SecurityException("Acesso negado. Apenas administradores podem cadastrar perfis.");
        }
        
        Perfil perfil = modelMapper.map(perfilRequest, Perfil.class);
        Perfil perfilSalvo = perfilRepository.save(perfil);
        return modelMapper.map(perfilSalvo, PerfilDto.class);
    }

    public PerfilDto atualizarPerfil(Long id, PerfilRequest perfilRequest) {
        log.info("Atualizando perfil com ID: {}", id);
        
        // Verificar se o administrador tem permissão para atualizar perfis
        if (perfilRequest.getAdminUsuario() == null || perfilRequest.getAdminSenha() == null) {
            throw new IllegalArgumentException("Credenciais do administrador são obrigatórias para atualizar perfis.");
        }
        
        boolean temPermissao = verificarPermissaoAtualizarPerfil(
            perfilRequest.getAdminUsuario(), 
            perfilRequest.getAdminSenha()
        );
        
        if (!temPermissao) {
            throw new SecurityException("Acesso negado. Apenas administradores podem atualizar perfis.");
        }
        
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

    public void removerPerfil(Long id, String adminUsuario, Integer adminSenha) {
        log.info("Removendo perfil com ID: {}", id);
        
        // Verificar se o administrador tem permissão para deletar perfis
        if (adminUsuario == null || adminSenha == null) {
            throw new IllegalArgumentException("Credenciais do administrador são obrigatórias para remover perfis.");
        }
        
        boolean temPermissao = verificarPermissaoDeletarPerfil(adminUsuario, adminSenha);
        
        if (!temPermissao) {
            throw new SecurityException("Acesso negado. Apenas administradores podem remover perfis.");
        }
        
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

    public boolean verificarPermissaoCadastrarFuncionario(String usuario, Integer senha) {
        log.info("Verificando permissão para cadastrar funcionário - usuário: {}", usuario);
        
        // Busca o funcionário pelo usuário e senha
        List<Funcionario> funcionarios = funcionarioRepository.findByUsuarioAndSenha(usuario, senha);
        
        if (funcionarios.isEmpty()) {
            log.warn("Funcionário não encontrado com as credenciais fornecidas");
            return false;
        }
        
        if (funcionarios.size() > 1) {
            log.warn("Múltiplos funcionários encontrados com as mesmas credenciais");
            return false;
        }
        
        Funcionario funcionario = funcionarios.get(0);
        Perfil perfil = funcionario.getPerfil();
        
        boolean temPermissao = perfil.isCadastrarFuncionario();
        log.info("Funcionário {} {} permissão para cadastrar funcionários", 
                funcionario.getNome(), temPermissao ? "TEM" : "NÃO TEM");
        
        return temPermissao;
    }

    public boolean verificarPermissaoCadastrarConsulta(String usuario, Integer senha) {
        log.info("Verificando permissão para cadastrar consulta - usuário: {}", usuario);
        return verificarPermissaoConsulta(usuario, senha, "cadastrarConsulta");
    }

    public boolean verificarPermissaoAtualizarConsulta(String usuario, Integer senha) {
        log.info("Verificando permissão para atualizar consulta - usuário: {}", usuario);
        return verificarPermissaoConsulta(usuario, senha, "atualizarConsulta");
    }

    public boolean verificarPermissaoDeletarConsulta(String usuario, Integer senha) {
        log.info("Verificando permissão para deletar consulta - usuário: {}", usuario);
        return verificarPermissaoConsulta(usuario, senha, "deletarConsulta");
    }

    private boolean verificarPermissaoConsulta(String usuario, Integer senha, String tipoPermissao) {
        // Busca o funcionário pelo usuário e senha
        List<Funcionario> funcionarios = funcionarioRepository.findByUsuarioAndSenha(usuario, senha);
        
        if (funcionarios.isEmpty()) {
            log.warn("Funcionário não encontrado com as credenciais fornecidas");
            return false;
        }
        
        if (funcionarios.size() > 1) {
            log.warn("Múltiplos funcionários encontrados com as mesmas credenciais");
            return false;
        }
        
        Funcionario funcionario = funcionarios.get(0);
        Perfil perfil = funcionario.getPerfil();
        
        boolean temPermissao = false;
        switch (tipoPermissao) {
            case "cadastrarConsulta":
                temPermissao = perfil.isCadastrarConsulta();
                break;
            case "atualizarConsulta":
                temPermissao = perfil.isAtualizarConsulta();
                break;
            case "deletarConsulta":
                temPermissao = perfil.isDeletarConsulta();
                break;
        }
        
        log.info("Funcionário {} {} permissão para {}", 
                funcionario.getNome(), temPermissao ? "TEM" : "NÃO TEM", tipoPermissao);
        
        return temPermissao;
    }

    public boolean verificarPermissaoCadastrarConvenio(String usuario, Integer senha) {
        log.info("Verificando permissão para cadastrar convênio - usuário: {}", usuario);
        return verificarPermissaoConvenio(usuario, senha, "cadastrarConvenio");
    }

    public boolean verificarPermissaoAtualizarConvenio(String usuario, Integer senha) {
        log.info("Verificando permissão para atualizar convênio - usuário: {}", usuario);
        return verificarPermissaoConvenio(usuario, senha, "atualizarConvenio");
    }

    public boolean verificarPermissaoDeletarConvenio(String usuario, Integer senha) {
        log.info("Verificando permissão para deletar convênio - usuário: {}", usuario);
        return verificarPermissaoConvenio(usuario, senha, "deletarConvenio");
    }

    private boolean verificarPermissaoConvenio(String usuario, Integer senha, String tipoPermissao) {
        // Busca o funcionário pelo usuário e senha
        List<Funcionario> funcionarios = funcionarioRepository.findByUsuarioAndSenha(usuario, senha);
        
        if (funcionarios.isEmpty()) {
            log.warn("Funcionário não encontrado com as credenciais fornecidas");
            return false;
        }
        
        if (funcionarios.size() > 1) {
            log.warn("Múltiplos funcionários encontrados com as mesmas credenciais");
            return false;
        }
        
        Funcionario funcionario = funcionarios.get(0);
        Perfil perfil = funcionario.getPerfil();
        
        boolean temPermissao = false;
        switch (tipoPermissao) {
            case "cadastrarConvenio":
                temPermissao = perfil.isCadastrarConvenio();
                break;
            case "atualizarConvenio":
                temPermissao = perfil.isAtualizarConvenio();
                break;
            case "deletarConvenio":
                temPermissao = perfil.isDeletarConvenio();
                break;
        }
        
        log.info("Funcionário {} {} permissão para {}", 
                funcionario.getNome(), temPermissao ? "TEM" : "NÃO TEM", tipoPermissao);
        
        return temPermissao;
    }

    public boolean verificarPermissaoCadastrarPerfil(String usuario, Integer senha) {
        log.info("Verificando permissão para cadastrar perfil - usuário: {}", usuario);
        return verificarPermissaoPerfil(usuario, senha, "cadastrarPerfil");
    }

    public boolean verificarPermissaoAtualizarPerfil(String usuario, Integer senha) {
        log.info("Verificando permissão para atualizar perfil - usuário: {}", usuario);
        return verificarPermissaoPerfil(usuario, senha, "atualizarPerfil");
    }

    public boolean verificarPermissaoDeletarPerfil(String usuario, Integer senha) {
        log.info("Verificando permissão para deletar perfil - usuário: {}", usuario);
        return verificarPermissaoPerfil(usuario, senha, "deletarPerfil");
    }

    private boolean verificarPermissaoPerfil(String usuario, Integer senha, String tipoPermissao) {
        log.info("Verificando permissão {} para usuário: {}", tipoPermissao, usuario);
        
        List<Funcionario> funcionarios = funcionarioRepository.findByUsuarioAndSenha(usuario, senha);
        
        if (funcionarios.isEmpty()) {
            log.warn("Funcionário não encontrado com as credenciais fornecidas");
            return false;
        }
        
        Funcionario funcionario = funcionarios.get(0);
        if (funcionario == null) {
            log.warn("Funcionário não encontrado ou credenciais inválidas para usuário: {}", usuario);
            return false;
        }
        
        Perfil perfil = funcionario.getPerfil();
        if (perfil == null) {
            log.warn("Perfil não encontrado para funcionário: {}", usuario);
            return false;
        }
        
        boolean temPermissao = switch (tipoPermissao) {
            case "cadastrarPerfil" -> perfil.isCadastrarPerfil();
            case "atualizarPerfil" -> perfil.isAtualizarPerfil();
            case "deletarPerfil" -> perfil.isDeletarPerfil();
            default -> false;
        };
        
        log.info("Permissão {} para usuário {}: {}", tipoPermissao, usuario, temPermissao ? "PERMITIDA" : "NEGADA");
        return temPermissao;
    }

    // Métodos para verificação de permissões de especialidades
    public boolean verificarPermissaoCadastrarEspecialidade(String usuario, Integer senha) {
        log.info("Verificando permissão para cadastrar especialidade");
        return verificarPermissaoEspecialidade(usuario, senha, "cadastrarEspecialidade");
    }

    public boolean verificarPermissaoAtualizarEspecialidade(String usuario, Integer senha) {
        log.info("Verificando permissão para atualizar especialidade");
        return verificarPermissaoEspecialidade(usuario, senha, "atualizarEspecialidade");
    }

    public boolean verificarPermissaoDeletarEspecialidade(String usuario, Integer senha) {
        log.info("Verificando permissão para deletar especialidade");
        return verificarPermissaoEspecialidade(usuario, senha, "deletarEspecialidade");
    }

    private boolean verificarPermissaoEspecialidade(String usuario, Integer senha, String tipoPermissao) {
        log.info("Verificando permissão {} para usuário: {}", tipoPermissao, usuario);
        
        List<Funcionario> funcionarios = funcionarioRepository.findByUsuarioAndSenha(usuario, senha);
        
        if (funcionarios.isEmpty()) {
            log.warn("Funcionário não encontrado com as credenciais fornecidas");
            return false;
        }
        
        if (funcionarios.size() > 1) {
            log.warn("Múltiplos funcionários encontrados com as mesmas credenciais");
            return false;
        }
        
        Funcionario funcionario = funcionarios.get(0);
        Perfil perfil = funcionario.getPerfil();
        
        if (perfil == null) {
            log.warn("Perfil não encontrado para funcionário: {}", usuario);
            return false;
        }
        
        boolean temPermissao = switch (tipoPermissao) {
            case "cadastrarEspecialidade" -> perfil.isCadastrarEspecialidade();
            case "atualizarEspecialidade" -> perfil.isAtualizarEspecialidade();
            case "deletarEspecialidade" -> perfil.isDeletarEspecialidade();
            default -> false;
        };
        
        log.info("Funcionário {} {} permissão para {}", 
                funcionario.getNome(), temPermissao ? "TEM" : "NÃO TEM", tipoPermissao);
        
        return temPermissao;
    }
}