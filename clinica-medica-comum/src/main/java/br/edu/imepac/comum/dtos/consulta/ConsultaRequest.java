package br.edu.imepac.comum.dtos.consulta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultaRequest {
    private String atendenteUsuario;
    private Integer atendenteSenha;
    private LocalDateTime dataHorario;
    private String sintomas;
    private Boolean eRetorno;
    private Boolean estaAtiva;
    private Long pacienteId;
    private Long medicoId;
    private Long convenioId;
}