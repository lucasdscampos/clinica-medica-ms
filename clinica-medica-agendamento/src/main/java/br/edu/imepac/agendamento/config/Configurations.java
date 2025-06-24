package br.edu.imepac.agendamento.config;

import br.edu.imepac.comum.dtos.consulta.ConsultaRequest;
import br.edu.imepac.comum.models.Consulta;
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

        modelMapper.createTypeMap(ConsultaRequest.class, Consulta.class)
                .addMappings(mapper -> {
                    mapper.map(ConsultaRequest::getERetorno, Consulta::setERetorno);
                    mapper.map(ConsultaRequest::getEstaAtiva, Consulta::setEstaAtiva);
                });

        return modelMapper;
    }
}