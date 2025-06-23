package br.edu.imepac.comum.dtos.funcionario;

import br.edu.imepac.comum.dtos.perfil.PerfilDto;
import br.edu.imepac.comum.dtos.especialidade.EspecialidadeDto;
import br.edu.imepac.comum.domain.EnumTipoFuncionario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuncionarioDto {
    private Long id;
    private String usuario;
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

    private PerfilDto perfil;
    private EnumTipoFuncionario tipoFuncionario;

    private EspecialidadeDto especialidade;
}