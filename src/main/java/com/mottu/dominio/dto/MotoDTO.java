package com.mottu.dominio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;

public class MotoDTO {

    private Long id;

    @NotBlank(message = "A placa é obrigatória")
    @Size(min = 7, max = 7, message = "A placa deve ter 7 caracteres")
    private String placa;

    @NotBlank(message = "O modelo é obrigatório")
    @Size(min = 2, max = 50, message = "O modelo deve ter entre 2 e 50 caracteres")
    private String modelo;

    @NotNull(message = "O ano é obrigatório")
    @Min(value = 1900, message = "O ano deve ser posterior a 1900")
    private Integer ano;

    @NotNull(message = "O valor da diária é obrigatório")
    @Min(value = 0, message = "O valor da diária não pode ser negativo")
    private Double valorDiaria;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Double getValorDiaria() {
        return valorDiaria;
    }

    public void setValorDiaria(Double valorDiaria) {
        this.valorDiaria = valorDiaria;
    }
}

