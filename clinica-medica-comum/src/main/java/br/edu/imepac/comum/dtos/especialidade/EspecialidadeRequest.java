package br.edu.imepac.comum.dtos.especialidade;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EspecialidadeRequest {
    private String adminUsuario;
    private Integer adminSenha;
    private String nome;
    private String descricao;
}