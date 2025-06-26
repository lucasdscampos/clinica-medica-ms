package br.edu.imepac.atendimento.controllers;

import br.edu.imepac.comum.dtos.prontuario.ProntuarioDto;
import br.edu.imepac.comum.dtos.prontuario.ProntuarioRequest;
import br.edu.imepac.comum.services.ProntuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/prontuarios")
public class ProntuarioController {

    private final ProntuarioService prontuarioService;

    public ProntuarioController(ProntuarioService prontuarioService) {
        this.prontuarioService = prontuarioService;
    }

    @PostMapping
    public ResponseEntity<ProntuarioDto> criarProntuario(@RequestBody @Valid ProntuarioRequest request) {
        log.info("Recebida requisição para criar prontuário para consulta ID: {}", request.getConsultaId());
        ProntuarioDto novoProntuario = prontuarioService.adicionarProntuario(request);
        log.info("Prontuário ID: {} criado com sucesso para consulta ID: {}", novoProntuario.getId(), request.getConsultaId());
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProntuario);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ProntuarioDto>> listarProntuarios() {
        log.info("Recebida requisição para listar todos os prontuários.");
        List<ProntuarioDto> prontuarios = prontuarioService.listarProntuarios();
        log.info("Listagem de prontuários concluída. Total: {} prontuários encontrados.", prontuarios.size());
        return ResponseEntity.ok(prontuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProntuarioDto> buscarProntuarioPorId(@PathVariable Long id) {
        log.info("Recebida requisição para buscar prontuário com ID: {}", id);
        ProntuarioDto prontuario = prontuarioService.buscarProntuarioPorId(id);
        log.info("Prontuário com ID: {} encontrado.", id);
        return ResponseEntity.ok(prontuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProntuarioDto> atualizarProntuario(@PathVariable Long id, @RequestBody @Valid ProntuarioRequest request) {
        log.info("Recebida requisição para atualizar prontuário com ID: {}", id);
        ProntuarioDto prontuarioAtualizado = prontuarioService.atualizarProntuario(id, request);
        log.info("Prontuário com ID: {} atualizado com sucesso.", id);
        return ResponseEntity.ok(prontuarioAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerProntuario(@PathVariable Long id) {
        log.info("Recebida requisição para remover prontuário com ID: {}", id);
        prontuarioService.removerProntuario(id);
        log.info("Prontuário com ID: {} removido com sucesso.", id);
        return ResponseEntity.noContent().build();
    }
}