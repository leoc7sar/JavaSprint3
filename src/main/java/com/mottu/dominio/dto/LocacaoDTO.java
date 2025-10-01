package com.mottu.dominio.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class LocacaoDTO {

    private Long id;

    @NotNull(message = "O entregador é obrigatório")
    private Long entregadorId;

    @NotNull(message = "A moto é obrigatória")
    private Long motoId;

    @NotNull(message = "A data de início é obrigatória")
    private LocalDate dataInicio;

    private LocalDate dataFim;

    private Double valorDiaria;

    private Double valorTotal;

    private String status;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEntregadorId() {
        return entregadorId;
    }

    public void setEntregadorId(Long entregadorId) {
        this.entregadorId = entregadorId;
    }

    public Long getMotoId() {
        return motoId;
    }

    public void setMotoId(Long motoId) {
        this.motoId = motoId;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Double getValorDiaria() {
        return valorDiaria;
    }

    public void setValorDiaria(Double valorDiaria) {
        this.valorDiaria = valorDiaria;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

