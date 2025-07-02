package br.edu.imepac.agendamento.controllers;

import br.edu.imepac.comum.dtos.consulta.ConsultaDto;
import br.edu.imepac.comum.dtos.consulta.ConsultaRequest;
import br.edu.imepac.comum.services.ConsultaService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @PostMapping
    public ResponseEntity<ConsultaDto> adicionarConsulta(@RequestBody @Valid ConsultaRequest request) {
        log.info("Recebida requisição para adicionar consulta para paciente ID: {}", request.getPacienteId());
        ConsultaDto novaConsulta = consultaService.adicionarConsulta(request);
        log.info("Consulta ID: {} para paciente ID: {} criada com sucesso.", novaConsulta.getId(), novaConsulta.getPaciente().getId());
        return new ResponseEntity<>(novaConsulta, HttpStatus.CREATED);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ConsultaDto>> listarConsultas() {
        log.info("Recebida requisição para listar todas as consultas.");
        List<ConsultaDto> consultas = consultaService.listarConsultas();
        log.info("Retornando {} consultas.", consultas.size());
        return new ResponseEntity<>(consultas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultaDto> buscarConsultaPorId(@PathVariable Long id) {
        log.info("Recebida requisição para buscar consulta com ID: {}", id);
        ConsultaDto consulta = consultaService.buscarConsultaPorId(id);
        log.info("Consulta com ID: {} encontrada.", id);
        return new ResponseEntity<>(consulta, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultaDto> atualizarConsulta(@PathVariable Long id, @RequestBody @Valid ConsultaRequest request) {
        log.info("Recebida requisição para atualizar consulta com ID: {}", id);
        ConsultaDto consultaAtualizada = consultaService.atualizarConsulta(id, request);
        log.info("Consulta com ID: {} atualizada com sucesso.", id);
        return new ResponseEntity<>(consultaAtualizada, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerConsulta(@PathVariable Long id, 
                                               @RequestParam String atendenteUsuario,
                                               @RequestParam Integer atendenteSenha) {
        log.info("Recebida requisição para remover consulta com ID: {}", id);
        consultaService.removerConsulta(id, atendenteUsuario, atendenteSenha);
        log.info("Consulta com ID: {} removida com sucesso.", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}