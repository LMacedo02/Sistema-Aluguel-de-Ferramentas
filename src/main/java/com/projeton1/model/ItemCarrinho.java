package com.projeton1.model;

public class ItemCarrinho {
    private Ferramenta ferramenta;
    private int dias;

    public ItemCarrinho() {}

    public ItemCarrinho(Ferramenta ferramenta, int dias) {
        this.ferramenta = ferramenta;
        this.dias = dias;
    }

    public double getSubtotalBruto() {
        return ferramenta.getPrecoDiaria() * dias;
    }

    public double getValorDesconto() {
        if (dias >= 5) {
            return getSubtotalBruto() * 0.05;
        }
        return 0.0;
    }

    // ESTE É O MÉTODO QUE O CONTROLLER ESTÁ PROCURANDO:
    public double getSubtotalLiquido() {
        return getSubtotalBruto() - getValorDesconto();
    }

    public Ferramenta getFerramenta() { return ferramenta; }
    public void setFerramenta(Ferramenta ferramenta) { this.ferramenta = ferramenta; }
    public int getDias() { return dias; }
    public void setDias(int dias) { this.dias = dias; }
}
