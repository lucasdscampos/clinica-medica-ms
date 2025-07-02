package br.edu.imepac.administrativo.controllers;

import br.edu.imepac.comum.dtos.especialidade.EspecialidadeDto;
import br.edu.imepac.comum.dtos.especialidade.EspecialidadeRequest;
import br.edu.imepac.comum.services.EspecialidadeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/especialidades")
public class EspecialdadeController {
    private final EspecialidadeService especialidadeService;

    public EspecialdadeController(EspecialidadeService especialidadeService) {
        this.especialidadeService = especialidadeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EspecialidadeDto criarEspecialidade(@RequestBody EspecialidadeRequest especialidadeRequest) {
        log.info("Recebida requisição para criar especialidade: {}", especialidadeRequest.getNome());
        return especialidadeService.adicionarEspecialidade(especialidadeRequest);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EspecialidadeDto atualizarEspecialidade(@PathVariable Long id, @RequestBody EspecialidadeRequest especialidadeRequest) {
        log.info("Recebida requisição para atualizar especialidade com ID: {}", id);
        return especialidadeService.atualizarEspecialidade(id, especialidadeRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerEspecialidade(@PathVariable Long id,
                                   @RequestParam String adminUsuario,
                                   @RequestParam Integer adminSenha) {
        log.info("Recebida requisição para remover especialidade com ID: {}", id);
        especialidadeService.removerEspecialidade(id, adminUsuario, adminSenha);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EspecialidadeDto buscarEspecialidadePorId(@PathVariable Long id) {
        log.info("Recebida requisição para buscar especialidade com ID: {}", id);
        return especialidadeService.buscarEspecialidadePorId(id);
    }

    @GetMapping("/listar")
    @ResponseStatus(HttpStatus.OK)
    public List<EspecialidadeDto> listarEspecialidades() {
        log.info("Recebida requisição para listar todas as especialidades.");
        return especialidadeService.listarEspecialidades();
    }
}
