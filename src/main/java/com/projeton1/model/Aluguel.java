package com.projeton1.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Aluguel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Ferramenta ferramenta;

    private Integer dias;
    private LocalDate dataAluguel;
    
    // CAMPOS NECESSÁRIOS PARA O SEU ALUGUELSERVICE:
    private Double valorTotal;
    private String status = "PENDENTE";

    public Aluguel() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Ferramenta getFerramenta() { return ferramenta; }
    public void setFerramenta(Ferramenta ferramenta) { this.ferramenta = ferramenta; }

    public Integer getDias() { return dias; }
    public void setDias(Integer dias) { this.dias = dias; }

    public LocalDate getDataAluguel() { return dataAluguel; }
    public void setDataAluguel(LocalDate dataAluguel) { this.dataAluguel = dataAluguel; }

    public Double getValorTotal() { return valorTotal; }
    public void setValorTotal(Double valorTotal) { this.valorTotal = valorTotal; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
