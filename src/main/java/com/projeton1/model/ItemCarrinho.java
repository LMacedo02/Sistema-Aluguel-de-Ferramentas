package com.projeton1.model;

import java.io.Serializable;

public class ItemCarrinho implements Serializable {
    private Ferramenta ferramenta;
    private Integer dias;
    private Double subtotal;

    public ItemCarrinho(Ferramenta ferramenta, Integer dias) {
        this.ferramenta = ferramenta;
        this.dias = dias;
        this.subtotal = calcularSubtotal();
    }

    public Double calcularSubtotal() {
        double valor = ferramenta.getPrecoDiaria() * dias;
        if (dias >= 5) valor *= 0.95; // Regra dos 5% de desconto
        return valor;
    }

    // Getters e Setters
    public Ferramenta getFerramenta() { return ferramenta; }
    public void setFerramenta(Ferramenta ferramenta) { this.ferramenta = ferramenta; }
    public Integer getDias() { return dias; }
    public void setDias(Integer dias) { this.dias = dias; this.subtotal = calcularSubtotal(); }
    public Double getSubtotal() { return subtotal; }
}
