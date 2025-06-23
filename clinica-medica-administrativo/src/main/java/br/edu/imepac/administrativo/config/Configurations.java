package br.edu.imepac.administrativo.config;

import br.edu.imepac.comum.models.Funcionario;
import br.edu.imepac.comum.dtos.funcionario.FuncionarioRequest;
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
                });

        return modelMapper;
    }
}