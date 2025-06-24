package br.edu.imepac.comum.dtos.consulta;

import br.edu.imepac.comum.dtos.paciente.PacienteDto;
import br.edu.imepac.comum.dtos.funcionario.FuncionarioDto;
import br.edu.imepac.comum.dtos.convenio.ConvenioDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultaDto {
    private Long id;
    private LocalDateTime dataHorario;
    private String sintomas;
    private Boolean eRetorno;
    private Boolean estaAtiva;

    private PacienteDto paciente;
    private FuncionarioDto medico;
    private ConvenioDto convenio;
}