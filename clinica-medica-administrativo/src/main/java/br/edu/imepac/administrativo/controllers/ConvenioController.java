package br.edu.imepac.administrativo.controllers;

import br.edu.imepac.comum.dtos.convenio.ConvenioDto;
import br.edu.imepac.comum.dtos.convenio.ConvenioRequest;
import br.edu.imepac.comum.services.ConvenioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/convenios")
public class ConvenioController {

    private final ConvenioService convenioService;

    public ConvenioController(ConvenioService convenioService) {
        this.convenioService = convenioService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConvenioDto criarConvenio(@RequestBody ConvenioRequest convenioRequest) {
        log.info("Recebida requisição para criar convênio: {}", convenioRequest.getNome());
        return convenioService.adicionarConvenio(convenioRequest);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ConvenioDto atualizarConvenio(@PathVariable Long id, @RequestBody ConvenioDto convenioDto) {
        log.info("Recebida requisição para atualizar convênio com ID: {}", id);
        return convenioService.atualizarConvenio(id, convenioDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerConvenio(@PathVariable Long id) {
        log.info("Recebida requisição para remover convênio com ID: {}", id);
        convenioService.removerConvenio(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ConvenioDto buscarConvenioPorId(@PathVariable Long id) {
        log.info("Recebida requisição para buscar convênio com ID: {}", id);
        return convenioService.buscarConvenioPorId(id);
    }

    @GetMapping("/listar")
    @ResponseStatus(HttpStatus.OK)
    public List<ConvenioDto> listarConvenios() {
        log.info("Recebida requisição para listar todos os convênios.");
        return convenioService.listarConvenios();
    }
}