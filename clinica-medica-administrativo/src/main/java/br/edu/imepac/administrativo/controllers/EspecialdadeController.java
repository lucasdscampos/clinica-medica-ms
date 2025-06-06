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
        log.info("Criando especialidade - controller: {}", especialidadeRequest);
        return especialidadeService.adicionarEspecialidade(especialidadeRequest);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EspecialidadeDto atualizarEspecialidade(@PathVariable Long id, @RequestBody EspecialidadeDto especialidadeDto) {
        return especialidadeService.atualizarEspecialidade(id, especialidadeDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerEspecialidade(@PathVariable Long id) {
        especialidadeService.removerEspecialidade(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EspecialidadeDto buscarEspecialidadePorId(@PathVariable Long id) {
        return especialidadeService.buscarEspecialidadePorId(id);
    }

    @GetMapping("/listar")
    @ResponseStatus(HttpStatus.OK)
    public List<EspecialidadeDto> listarEspecialidades() {
        return especialidadeService.listarEspecialidades();
    }
}
