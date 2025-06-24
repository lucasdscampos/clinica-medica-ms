package br.edu.imepac.agendamento.controllers;

import br.edu.imepac.comum.dtos.paciente.PacienteDto;
import br.edu.imepac.comum.dtos.paciente.PacienteRequest;
import br.edu.imepac.comum.services.PacienteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PostMapping
    public ResponseEntity<PacienteDto> criarPaciente(@RequestBody PacienteRequest request) {
        log.info("Recebendo requisição para criar paciente: {}", request.getNome());
        PacienteDto novoPaciente = pacienteService.adicionarPaciente(request);
        log.info("Paciente criado com sucesso. ID: {}", novoPaciente.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPaciente);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<PacienteDto>> listarPacientes() {
        log.info("Recebendo requisição para listar todos os pacientes.");
        List<PacienteDto> pacientes = pacienteService.listarPacientes();
        log.info("Listagem de pacientes concluída. Total: {}", pacientes.size());
        return ResponseEntity.ok(pacientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteDto> buscarPacientePorId(@PathVariable Long id) {
        log.info("Recebendo requisição para buscar paciente por ID: {}", id);
        PacienteDto paciente = pacienteService.buscarPacientePorId(id);
        log.info("Paciente com ID {} encontrado.", id);
        return ResponseEntity.ok(paciente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteDto> atualizarPaciente(@PathVariable Long id, @RequestBody PacienteDto pacienteDto) {
        log.info("Recebendo requisição para atualizar paciente com ID: {}", id);
        PacienteDto pacienteAtualizado = pacienteService.atualizarPaciente(id, pacienteDto);
        log.info("Paciente com ID {} atualizado com sucesso.", id);
        return ResponseEntity.ok(pacienteAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerPaciente(@PathVariable Long id) {
        log.info("Recebendo requisição para remover paciente com ID: {}", id);
        pacienteService.removerPaciente(id);
        log.info("Paciente com ID {} removido com sucesso.", id);
        return ResponseEntity.noContent().build();
    }
}