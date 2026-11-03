package br.edu.insper.projeto_final_objetos.agendamento;

import br.edu.insper.projeto_final_objetos.cliente.Cliente;
import br.edu.insper.projeto_final_objetos.profissional.Profissional;
import br.edu.insper.projeto_final_objetos.servico.Servico;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Agendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StatusAgendamento status = StatusAgendamento.PENDENTE;

    @Column(nullable = false)
    private boolean ativo = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    // Fica nulo enquanto nenhum profissional aceitar a solicitação.
    @ManyToOne
    @JoinColumn(name = "id_profissional")
    private Profissional profissional;

    @ManyToMany
    @JoinTable(name = "agendamento_servico",
            joinColumns = @JoinColumn(name = "id_agendamento"),
            inverseJoinColumns = @JoinColumn(name = "id_servico"))
    private List<Servico> servicos = new ArrayList<>();

    @PrePersist
    void inicializarStatus() {
        if (status == null) status = StatusAgendamento.PENDENTE;
    }

    public Integer getId() { return id; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public StatusAgendamento getStatus() { return status; }
    public void setStatus(StatusAgendamento status) { this.status = status; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Profissional getProfissional() { return profissional; }
    public void setProfissional(Profissional profissional) { this.profissional = profissional; }
    public List<Servico> getServicos() { return servicos; }
}
