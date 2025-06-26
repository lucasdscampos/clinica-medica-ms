package br.edu.imepac.comum.dtos.prontuario;

import br.edu.imepac.comum.dtos.consulta.ConsultaDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProntuarioDto {
    private Long id;
    private String receituario;
    private String exames;
    private String observacoes;
    private ConsultaDto consulta;
}