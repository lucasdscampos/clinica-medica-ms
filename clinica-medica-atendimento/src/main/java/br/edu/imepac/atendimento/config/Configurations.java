package br.edu.imepac.atendimento.config;

import br.edu.imepac.comum.dtos.prontuario.ProntuarioDto;
import br.edu.imepac.comum.models.Prontuario;
import br.edu.imepac.comum.models.Consulta;
import br.edu.imepac.comum.dtos.consulta.ConsultaDto;
import br.edu.imepac.comum.models.Paciente;
import br.edu.imepac.comum.dtos.paciente.PacienteDto;
import br.edu.imepac.comum.models.Funcionario;
import br.edu.imepac.comum.dtos.funcionario.FuncionarioDto;
import br.edu.imepac.comum.models.Convenio;
import br.edu.imepac.comum.dtos.convenio.ConvenioDto;
import br.edu.imepac.comum.models.Especialidade;
import br.edu.imepac.comum.dtos.especialidade.EspecialidadeDto;
import br.edu.imepac.comum.models.Perfil;
import br.edu.imepac.comum.dtos.perfil.PerfilDto;
import br.edu.imepac.comum.domain.EnumTipoFuncionario;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.modelmapper.AbstractConverter;

@Configuration
public class Configurations {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.createTypeMap(Paciente.class, PacienteDto.class).implicitMappings();

        modelMapper.createTypeMap(Convenio.class, ConvenioDto.class).implicitMappings();

        modelMapper.createTypeMap(Especialidade.class, EspecialidadeDto.class).implicitMappings();

        modelMapper.createTypeMap(Perfil.class, PerfilDto.class).implicitMappings();

        modelMapper.addConverter(new AbstractConverter<Funcionario, FuncionarioDto>() {
            @Override
            protected FuncionarioDto convert(Funcionario source) {
                if (source == null) {
                    return null;
                }

                FuncionarioDto dto = new FuncionarioDto();

                dto.setId(source.getId());
                dto.setUsuario(source.getUsuario());
                // dto.setSenha(source.getSenha());
                dto.setNome(source.getNome());
                dto.setIdade(source.getIdade());
                dto.setSexo(source.getSexo());
                dto.setCpf(source.getCpf());
                dto.setRua(source.getRua());
                dto.setNumero(source.getNumero());
                dto.setComplemento(source.getComplemento());
                dto.setBairro(source.getBairro());
                dto.setCidade(source.getCidade());
                dto.setEstado(source.getEstado());
                dto.setContato(source.getContato());
                dto.setEmail(source.getEmail());
                dto.setDataNascimento(source.getDataNascimento());

                if (source.getTipoFuncionario() != null) {
                    dto.setTipoFuncionario(source.getTipoFuncionario());
                }

                if (source.getPerfil() != null) {
                    dto.setPerfil(modelMapper.map(source.getPerfil(), PerfilDto.class));
                }

                if (source.getTipoFuncionario() != null && EnumTipoFuncionario.MEDICO.name().equals(source.getTipoFuncionario()) && source.getEspecialidade() != null) {
                    dto.setEspecialidade(modelMapper.map(source.getEspecialidade(), EspecialidadeDto.class));
                } else {
                    dto.setEspecialidade(null);
                }

                return dto;
            }
        });

        modelMapper.createTypeMap(Consulta.class, ConsultaDto.class).implicitMappings();

        modelMapper.createTypeMap(Prontuario.class, ProntuarioDto.class).implicitMappings();

        return modelMapper;
    }
}