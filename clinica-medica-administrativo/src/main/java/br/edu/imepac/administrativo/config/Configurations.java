package br.edu.imepac.administrativo.config;

import br.edu.imepac.comum.models.Funcionario;
import br.edu.imepac.comum.models.Convenio;
import br.edu.imepac.comum.models.Perfil;
import br.edu.imepac.comum.dtos.funcionario.FuncionarioRequest;
import br.edu.imepac.comum.dtos.convenio.ConvenioRequest;
import br.edu.imepac.comum.dtos.perfil.PerfilRequest;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Configurations {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.createTypeMap(FuncionarioRequest.class, Funcionario.class)
                .addMappings(mapper -> {
                    mapper.skip(Funcionario::setId);
                    mapper.skip(Funcionario::setPerfil);
                    mapper.skip(Funcionario::setEspecialidade);
                    // Ignorar campos de autenticação do administrador
                    mapper.skip((dest, v) -> dest.setUsuario(null)); // será mapeado corretamente pelo campo 'usuario'
                });

        modelMapper.createTypeMap(ConvenioRequest.class, Convenio.class)
                .addMappings(mapper -> {
                    mapper.skip(Convenio::setId);
                    // Os campos adminUsuario e adminSenha do ConvenioRequest não são mapeados para o modelo Convenio
                });

        modelMapper.createTypeMap(PerfilRequest.class, Perfil.class)
                .addMappings(mapper -> {
                    mapper.skip(Perfil::setId);
                    // Os campos adminUsuario e adminSenha do PerfilRequest não são mapeados para o modelo Perfil
                });

        return modelMapper;
    }
}