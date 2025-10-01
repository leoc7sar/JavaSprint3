package com.mottu.dominio.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class PagamentoDTO {

    private Long id;

    @NotNull(message = "A locação é obrigatória")
    private Long locacaoId;

    @NotNull(message = "O valor do pagamento é obrigatório")
    @Min(value = 0, message = "O valor do pagamento não pode ser negativo")
    private Double valorPago;

    @NotNull(message = "A data do pagamento é obrigatória")
    private LocalDate dataPagamento;

    @NotBlank(message = "O método de pagamento é obrigatório")
    private String metodo;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLocacaoId() {
        return locacaoId;
    }

    public void setLocacaoId(Long locacaoId) {
        this.locacaoId = locacaoId;
    }

    public Double getValorPago() {
        return valorPago;
    }

    public void setValorPago(Double valorPago) {
        this.valorPago = valorPago;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }
}

