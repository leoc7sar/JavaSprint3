package com.mottu.servico;

import com.mottu.dominio.Moto;
import com.mottu.dominio.dto.MotoDTO;
import com.mottu.repositorio.MotoRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
public class MotoService {

    private final MotoRepositorio motoRepositorio;

    public MotoService(MotoRepositorio motoRepositorio) {
        this.motoRepositorio = motoRepositorio;
    }

    @Transactional(readOnly = true)
    public List<MotoDTO> findAll() {
        return motoRepositorio.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<MotoDTO> findById(Long id) {
        return motoRepositorio.findById(id)
                .map(this::convertToDto);
    }

    @Transactional
    public MotoDTO save(MotoDTO motoDTO) {
        Moto moto = convertToEntity(motoDTO);
        moto = motoRepositorio.save(moto);
        return convertToDto(moto);
    }

    @Transactional
    public void deleteById(Long id) {
        motoRepositorio.deleteById(id);
    }

    private MotoDTO convertToDto(Moto moto) {
        MotoDTO dto = new MotoDTO();
        dto.setId(moto.getId());
        dto.setPlaca(moto.getPlaca());
        dto.setModelo(moto.getModelo());
        dto.setAno(moto.getAno());
        dto.setValorDiaria(moto.getValorDiaria() != null ? moto.getValorDiaria().doubleValue() : null);
        return dto;
    }

    private Moto convertToEntity(MotoDTO dto) {
        Moto moto = new Moto();
        moto.setId(dto.getId());
        moto.setPlaca(dto.getPlaca());
        moto.setModelo(dto.getModelo());
        moto.setAno(dto.getAno());
        moto.setValorDiaria(dto.getValorDiaria() != null ? BigDecimal.valueOf(dto.getValorDiaria()) : null);
        return moto;
    }
}

