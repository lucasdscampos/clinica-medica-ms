package br.edu.imepac.comum.dtos.convenio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConvenioRequest {
    private String nome;
    private String descricao;
}