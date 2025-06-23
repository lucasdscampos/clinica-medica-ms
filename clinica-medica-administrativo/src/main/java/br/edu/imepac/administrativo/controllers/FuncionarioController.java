package br.edu.imepac.administrativo.controllers;

import br.edu.imepac.comum.dtos.funcionario.FuncionarioDto;
import br.edu.imepac.comum.dtos.funcionario.FuncionarioRequest;
import br.edu.imepac.comum.services.FuncionarioService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    @PostMapping
    public ResponseEntity<FuncionarioDto> adicionarFuncionario(@RequestBody @Valid FuncionarioRequest request) {
        log.info("Recebida requisição para adicionar funcionário: {}", request.getNome());
        FuncionarioDto novoFuncionario = funcionarioService.adicionarFuncionario(request);
        log.info("Funcionário {} criado com sucesso, ID: {}", novoFuncionario.getNome(), novoFuncionario.getId());
        return new ResponseEntity<>(novoFuncionario, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FuncionarioDto>> listarFuncionarios() {
        log.info("Recebida requisição para listar todos os funcionários.");
        List<FuncionarioDto> funcionarios = funcionarioService.listarFuncionarios();
        log.info("Retornando {} funcionários.", funcionarios.size());
        return new ResponseEntity<>(funcionarios, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioDto> buscarFuncionarioPorId(@PathVariable Long id) {
        log.info("Recebida requisição para buscar funcionário com ID: {}", id);
        FuncionarioDto funcionario = funcionarioService.buscarFuncionarioPorId(id);
        log.info("Funcionário com ID: {} encontrado.", id);
        return new ResponseEntity<>(funcionario, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioDto> atualizarFuncionario(@PathVariable Long id, @RequestBody @Valid FuncionarioRequest request) {
        log.info("Recebida requisição para atualizar funcionário com ID: {}", id);
        FuncionarioDto funcionarioAtualizado = funcionarioService.atualizarFuncionario(id, request);
        log.info("Funcionário com ID: {} atualizado com sucesso.", id);
        return new ResponseEntity<>(funcionarioAtualizado, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerFuncionario(@PathVariable Long id) {
        log.info("Recebida requisição para remover funcionário com ID: {}", id);
        funcionarioService.removerFuncionario(id);
        log.info("Funcionário com ID: {} removido com sucesso.", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}