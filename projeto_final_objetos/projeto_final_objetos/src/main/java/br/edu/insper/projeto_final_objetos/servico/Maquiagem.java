package br.edu.insper.projeto_final_objetos.servico;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("MAQUIAGEM")
public class Maquiagem extends Servico {
    public Maquiagem() {}
}