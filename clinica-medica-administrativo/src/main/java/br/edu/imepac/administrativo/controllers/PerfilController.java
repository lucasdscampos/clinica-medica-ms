package br.edu.imepac.administrativo.controllers;

import br.edu.imepac.comum.dtos.perfil.PerfilDto;
import br.edu.imepac.comum.dtos.perfil.PerfilRequest;
import br.edu.imepac.comum.services.PerfilService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/perfis")
public class PerfilController {

    private final PerfilService perfilService;

    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PerfilDto> adicionarPerfil(@RequestBody PerfilRequest perfilRequest) {
        log.info("Recebida requisição para adicionar perfil: {}", perfilRequest);
        PerfilDto novoPerfil = perfilService.adicionarPerfil(perfilRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPerfil);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<PerfilDto>> listarPerfis() {
        log.info("Recebida requisição para listar todos os perfis");
        List<PerfilDto> perfis = perfilService.listarPerfis();
        return ResponseEntity.ok(perfis);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerfilDto> buscarPerfilPorId(@PathVariable Long id) {
        log.info("Recebida requisição para buscar perfil com ID: {}", id);
        PerfilDto perfil = perfilService.buscarPerfilPorId(id);
        return ResponseEntity.ok(perfil);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerfilDto> atualizarPerfil(@PathVariable Long id, @RequestBody PerfilRequest perfilRequest) {
        log.info("Recebida requisição para atualizar perfil com ID: {}", id);
        PerfilDto perfilAtualizado = perfilService.atualizarPerfil(id, perfilRequest);
        return ResponseEntity.ok(perfilAtualizado);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> removerPerfil(@PathVariable Long id) {
        log.info("Recebida requisição para remover perfil com ID: {}", id);
        perfilService.removerPerfil(id);
        return ResponseEntity.noContent().build();
    }
}