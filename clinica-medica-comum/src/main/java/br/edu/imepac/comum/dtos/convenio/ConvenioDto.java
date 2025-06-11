package br.edu.imepac.comum.dtos.convenio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConvenioDto {
    private Long id;
    private String nome;
    private String descricao;
}