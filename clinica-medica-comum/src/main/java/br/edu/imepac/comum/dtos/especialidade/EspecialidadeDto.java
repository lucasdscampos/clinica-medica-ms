package br.edu.imepac.comum.dtos.especialidade;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EspecialidadeDto {
    private Long id;
    private String nome;
}