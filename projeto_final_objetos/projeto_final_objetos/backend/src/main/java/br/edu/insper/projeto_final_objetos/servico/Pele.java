package br.edu.insper.projeto_final_objetos.servico;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;


@Entity
@DiscriminatorValue("PELE")
public class Pele extends Servico {
    public Pele() {}
}