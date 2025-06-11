package br.edu.imepac.comum.services;

import br.edu.imepac.comum.dtos.convenio.ConvenioDto;
import br.edu.imepac.comum.dtos.convenio.ConvenioRequest;
import br.edu.imepac.comum.models.Convenio;
import br.edu.imepac.comum.repositories.ConvenioRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ConvenioService {

    private final ModelMapper modelMapper;
    private final ConvenioRepository convenioRepository;

    public ConvenioService(ModelMapper modelMapper, ConvenioRepository convenioRepository) {
        this.modelMapper = modelMapper;
        this.convenioRepository = convenioRepository;
    }

    public ConvenioDto adicionarConvenio(ConvenioRequest convenioRequest) {
        log.info("Adicionando novo convênio com nome: {}", convenioRequest.getNome());
        Convenio convenio = modelMapper.map(convenioRequest, Convenio.class);
        Convenio convenioSalvo = convenioRepository.save(convenio);
        return modelMapper.map(convenioSalvo, ConvenioDto.class);
    }

    public List<ConvenioDto> listarConvenios() {
        log.info("Listando todos os convênios.");
        List<Convenio> convenios = convenioRepository.findAll();
        return convenios.stream()
                .map(convenio -> modelMapper.map(convenio, ConvenioDto.class))
                .collect(Collectors.toList());
    }

    public ConvenioDto buscarConvenioPorId(Long id) {
        log.info("Buscando convênio com ID: {}", id);
        Convenio convenio = convenioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Convênio não encontrado com ID: " + id));
        return modelMapper.map(convenio, ConvenioDto.class);
    }

    public ConvenioDto atualizarConvenio(Long id, ConvenioDto convenioDto) {
        log.info("Atualizando convênio com ID: {}", id);

        Convenio convenioExistente = convenioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Convênio não encontrado com ID: " + id));

        modelMapper.map(convenioDto, convenioExistente);

        Convenio convenioAtualizado = convenioRepository.save(convenioExistente);
        return modelMapper.map(convenioAtualizado, ConvenioDto.class);
    }

    public void removerConvenio(Long id) {
        log.info("Removendo convênio com ID: {}", id);
        Convenio convenio = convenioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Convênio não encontrado com ID: " + id));
        convenioRepository.delete(convenio);
    }
}