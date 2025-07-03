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
                    mapper.skip(Funcionario::setTipoFuncionario);
                })
                .implicitMappings();

        modelMapper.createTypeMap(ConvenioRequest.class, Convenio.class)
                .addMappings(mapper -> {
                    mapper.skip(Convenio::setId);
                })
                .implicitMappings();

        modelMapper.createTypeMap(PerfilRequest.class, Perfil.class)
                .addMappings(mapper -> {
                    mapper.skip(Perfil::setId);
                })
                .implicitMappings();

        return modelMapper;
    }
}
