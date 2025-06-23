package br.edu.imepac.comum.dtos.funcionario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuncionarioRequest {
    private String usuario;
    private Integer senha;
    private String nome;
    private Integer idade;
    private Character sexo;
    private String cpf;
    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String contato;
    private String email;
    private LocalDate dataNascimento;

    private Long idPerfil;
    private String tipoFuncionario;

    private Long idEspecialidade;
}