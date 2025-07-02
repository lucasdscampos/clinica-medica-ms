package br.edu.imepac.comum.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "perfis")
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    @Column(name = "cadastrar_funcionario", nullable = false)
    private boolean cadastrarFuncionario;

    @Column(name = "ler_funcionario", nullable = false)
    private boolean lerFuncionario;

    @Column(name = "atualizar_funcionario", nullable = false)
    private boolean atualizarFuncionario;

    @Column(name = "deletar_funcionario", nullable = false)
    private boolean deletarFuncionario;

    @Column(name = "listar_funcionario", nullable = false)
    private boolean listarFuncionario;

    @Column(name = "cadastrar_paciente", nullable = false)
    private boolean cadastrarPaciente;

    @Column(name = "ler_paciente", nullable = false)
    private boolean lerPaciente;

    @Column(name = "atualizar_paciente", nullable = false)
    private boolean atualizarPaciente;

    @Column(name = "deletar_paciente", nullable = false)
    private boolean deletarPaciente;

    @Column(name = "listar_paciente", nullable = false)
    private boolean listarPaciente;

    @Column(name = "cadastrar_consulta", nullable = false)
    private boolean cadastrarConsulta;

    @Column(name = "ler_consulta", nullable = false)
    private boolean lerConsulta;

    @Column(name = "atualizar_consulta", nullable = false)
    private boolean atualizarConsulta;

    @Column(name = "deletar_consulta", nullable = false)
    private boolean deletarConsulta;

    @Column(name = "listar_consulta", nullable = false)
    private boolean listarConsulta;

    @Column(name = "cadastrar_especialidade", nullable = false)
    private boolean cadastrarEspecialidade;

    @Column(name = "ler_especialidade", nullable = false)
    private boolean lerEspecialidade;

    @Column(name = "atualizar_especialidade", nullable = false)
    private boolean atualizarEspecialidade;

    @Column(name = "deletar_especialidade", nullable = false)
    private boolean deletarEspecialidade;

    @Column(name = "listar_especialidade", nullable = false)
    private boolean listarEspecialidade;

    @Column(name = "cadastrar_convenio", nullable = false)
    private boolean cadastrarConvenio;

    @Column(name = "ler_convenio", nullable = false)
    private boolean lerConvenio;

    @Column(name = "atualizar_convenio", nullable = false)
    private boolean atualizarConvenio;

    @Column(name = "deletar_convenio", nullable = false)
    private boolean deletarConvenio;

    @Column(name = "listar_convenio", nullable = false)
    private boolean listarConvenio;

    @Column(name = "cadastrar_prontuario", nullable = false)
    private boolean cadastrarProntuario;

    @Column(name = "ler_prontuario", nullable = false)
    private boolean lerProntuario;

    @Column(name = "atualizar_prontuario", nullable = false)
    private boolean atualizarProntuario;

    @Column(name = "deletar_prontuario", nullable = false)
    private boolean deletarProntuario;

    @Column(name = "listar_prontuario", nullable = false)
    private boolean listarProntuario;

    @Column(name = "cadastrar_perfil", nullable = false)
    private boolean cadastrarPerfil;

    @Column(name = "ler_perfil", nullable = false)
    private boolean lerPerfil;

    @Column(name = "atualizar_perfil", nullable = false)
    private boolean atualizarPerfil;

    @Column(name = "deletar_perfil", nullable = false)
    private boolean deletarPerfil;

    @Column(name = "listar_perfil", nullable = false)
    private boolean listarPerfil;
}