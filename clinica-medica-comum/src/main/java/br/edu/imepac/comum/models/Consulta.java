package br.edu.imepac.comum.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "consultas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_horario", nullable = false)
    private LocalDateTime dataHorario;

    @Column(name = "sintomas", length = 500)
    private String sintomas;

    @Column(name = "e_retorno", nullable = false)
    private Boolean eRetorno;

    @Column(name = "esta_ativa", nullable = false)
    private Boolean estaAtiva;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private Funcionario medico;

    @ManyToOne
    @JoinColumn(name = "convenio_id")
    private Convenio convenio;
}