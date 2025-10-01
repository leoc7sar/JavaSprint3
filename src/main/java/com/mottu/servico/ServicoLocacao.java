package com.mottu.servico;

import com.mottu.dominio.*;
import com.mottu.dominio.dto.LocacaoDTO;
import com.mottu.dominio.dto.PagamentoDTO;
import com.mottu.repositorio.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServicoLocacao {
  private final LocacaoRepositorio locacaoRepositorio;
  private final MotoRepositorio motoRepositorio;
  private final EntregadorRepositorio entregadorRepositorio;
  private final PagamentoRepositorio pagamentoRepositorio;

  public ServicoLocacao(LocacaoRepositorio locacaoRepositorio, MotoRepositorio motoRepositorio, EntregadorRepositorio entregadorRepositorio, PagamentoRepositorio pagamentoRepositorio){
    this.locacaoRepositorio = locacaoRepositorio;
    this.motoRepositorio = motoRepositorio;
    this.entregadorRepositorio = entregadorRepositorio;
    this.pagamentoRepositorio = pagamentoRepositorio;
  }

  @Transactional(readOnly = true)
  public List<LocacaoDTO> findAll() {
      return locacaoRepositorio.findAll().stream()
              .map(this::convertToDto)
              .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public Optional<LocacaoDTO> findById(Long id) {
      return locacaoRepositorio.findById(id)
              .map(this::convertToDto);
  }

  @Transactional
  public LocacaoDTO abrirLocacao(LocacaoDTO locacaoDTO){
    Entregador entregador = entregadorRepositorio.findById(locacaoDTO.getEntregadorId())
            .orElseThrow(() -> new IllegalArgumentException("Entregador não encontrado"));
    Moto moto = motoRepositorio.findById(locacaoDTO.getMotoId())
            .orElseThrow(() -> new IllegalArgumentException("Moto não encontrada"));

    if(!moto.isDisponivel()) {
        throw new IllegalStateException("Moto indisponível para locação");
    }

    moto.setDisponivel(false);
    motoRepositorio.save(moto);

    Locacao locacao = new Locacao();
    locacao.setEntregador(entregador);
    locacao.setMoto(moto);
    locacao.setDataInicio(locacaoDTO.getDataInicio());
    // O valor da diária deve vir da moto, não do DTO de locação diretamente, para evitar inconsistências
    locacao.setValorDiaria(moto.getValorDiaria());
    locacao.setStatus(Locacao.Status.ABERTA);

    locacao = locacaoRepositorio.save(locacao);
    return convertToDto(locacao);
  }

  @Transactional
  public PagamentoDTO fecharLocacaoEPagar(Long locacaoId, LocalDate dataFim, Pagamento.Metodo metodo){
    Locacao locacao = locacaoRepositorio.findById(locacaoId)
            .orElseThrow(() -> new IllegalArgumentException("Locação não encontrada"));

    if(locacao.getStatus() == Locacao.Status.ENCERRADA) {
        throw new IllegalStateException("Locação já encerrada");
    }

    if (dataFim.isBefore(locacao.getDataInicio())) {
        throw new IllegalArgumentException("A data de fim não pode ser anterior à data de início da locação.");
    }

    long dias = Math.max(1, ChronoUnit.DAYS.between(locacao.getDataInicio(), dataFim));
    BigDecimal total = locacao.getValorDiaria().multiply(BigDecimal.valueOf(dias));

    locacao.setDataFim(dataFim);
    locacao.setTotal(total);
    locacao.setStatus(Locacao.Status.ENCERRADA);
    locacaoRepositorio.save(locacao);

    Moto moto = locacao.getMoto();
    moto.setDisponivel(true);
    motoRepositorio.save(moto);

    Pagamento pagamento = new Pagamento();
    pagamento.setLocacao(locacao);
    pagamento.setValor(total);
    pagamento.setDataPagamento(LocalDateTime.now());
    pagamento.setMetodo(metodo);
    pagamento = pagamentoRepositorio.save(pagamento);

    return convertToDto(pagamento);
  }

  private LocacaoDTO convertToDto(Locacao locacao) {
      LocacaoDTO dto = new LocacaoDTO();
      dto.setId(locacao.getId());
      dto.setEntregadorId(locacao.getEntregador().getId());
      dto.setMotoId(locacao.getMoto().getId());
      dto.setDataInicio(locacao.getDataInicio());
      dto.setDataFim(locacao.getDataFim());
      dto.setValorDiaria(locacao.getValorDiaria() != null ? locacao.getValorDiaria().doubleValue() : null);
      dto.setValorTotal(locacao.getTotal() != null ? locacao.getTotal().doubleValue() : null);
      dto.setStatus(locacao.getStatus().name());
      return dto;
  }

  private PagamentoDTO convertToDto(Pagamento pagamento) {
      PagamentoDTO dto = new PagamentoDTO();
      dto.setId(pagamento.getId());
      dto.setLocacaoId(pagamento.getLocacao().getId());
      dto.setValorPago(pagamento.getValor().doubleValue());
      dto.setDataPagamento(pagamento.getDataPagamento().toLocalDate());
      return dto;
  }
}

