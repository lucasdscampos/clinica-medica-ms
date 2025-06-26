package br.edu.imepac.comum.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "prontuarios")
public class Prontuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "receituario", columnDefinition = "TEXT")
    private String receituario;

    @Column(name = "exames", columnDefinition = "TEXT")
    private String exames;

    @Column(name = "observacoes", length = 500)
    private String observacoes;

    @OneToOne
    @JoinColumn(name = "consulta_id", unique = true, nullable = false)
    private Consulta consulta;
}