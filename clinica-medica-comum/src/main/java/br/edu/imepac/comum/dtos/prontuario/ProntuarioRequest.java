package br.edu.imepac.comum.dtos.prontuario;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProntuarioRequest {
    private String receituario;
    private String exames;
    private String observacoes;

    @NotNull(message = "O ID da consulta é obrigatório.")
    private Long consultaId;
}