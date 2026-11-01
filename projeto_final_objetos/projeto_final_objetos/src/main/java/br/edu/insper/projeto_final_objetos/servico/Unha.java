package br.edu.insper.projeto_final_objetos.servico;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("UNHA")
public class Unha extends Servico {

    public Unha() {
    }
}