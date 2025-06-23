package br.edu.imepac.comum.models;

import br.edu.imepac.comum.domain.EnumTipoFuncionario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "funcionarios")
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario", nullable = false, unique = true)
    private String usuario;

    @Column(name = "senha", nullable = false)
    private Integer senha;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "idade")
    private Integer idade;

    @Column(name = "sexo", length = 1)
    private Character sexo;

    @Column(name = "cpf", nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(name = "rua")
    private String rua;

    @Column(name = "numero")
    private String numero;

    @Column(name = "complemento")
    private String complemento;

    @Column(name = "bairro")
    private String bairro;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "estado")
    private String estado;

    @Column(name = "contato")
    private String contato;

    @Column(name = "email")
    private String email;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @ManyToOne
    @JoinColumn(name = "perfil_id", nullable = false)
    private Perfil perfil;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_funcionario", nullable = false)
    private EnumTipoFuncionario tipoFuncionario;

    @ManyToOne
    @JoinColumn(name = "especialidade_id", nullable = true)
    private Especialidade especialidade;
}