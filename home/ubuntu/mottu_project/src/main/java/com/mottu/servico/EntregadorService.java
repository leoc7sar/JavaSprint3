package com.mottu.servico;

import com.mottu.dominio.Entregador;
import com.mottu.dominio.dto.EntregadorDTO;
import com.mottu.repositorio.EntregadorRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EntregadorService {

    private final EntregadorRepositorio entregadorRepositorio;

    public EntregadorService(EntregadorRepositorio entregadorRepositorio) {
        this.entregadorRepositorio = entregadorRepositorio;
    }

    @Transactional(readOnly = true)
    public List<EntregadorDTO> findAll() {
        return entregadorRepositorio.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<EntregadorDTO> findById(Long id) {
        return entregadorRepositorio.findById(id)
                .map(this::convertToDto);
    }

    @Transactional
    public EntregadorDTO save(EntregadorDTO entregadorDTO) {
        Entregador entregador = convertToEntity(entregadorDTO);
        entregador = entregadorRepositorio.save(entregador);
        return convertToDto(entregador);
    }

    @Transactional
    public void deleteById(Long id) {
        entregadorRepositorio.deleteById(id);
    }

    private EntregadorDTO convertToDto(Entregador entregador) {
        EntregadorDTO dto = new EntregadorDTO();
        dto.setId(entregador.getId());
        dto.setNome(entregador.getNome());
        dto.setCpf(entregador.getCpf());
        dto.setEmail(entregador.getEmail());
        dto.setCnh(entregador.getCnh());
        return dto;
    }

    private Entregador convertToEntity(EntregadorDTO dto) {
        Entregador entregador = new Entregador();
        entregador.setId(dto.getId());
        entregador.setNome(dto.getNome());
        entregador.setCpf(dto.getCpf());
        entregador.setEmail(dto.getEmail());
        entregador.setCnh(dto.getCnh());
        return entregador;
    }
}

