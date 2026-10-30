package br.edu.insper.projeto_final_objetos.servico;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CABELO")
public class Cabelo extends Servico {
    public Cabelo() {
    }
}