package br.edu.insper.projeto_final_objetos.servico;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;


@Entity
@DiscriminatorValue("SOBRANCELHA")
public class Sobrancelha extends Servico {
    public Sobrancelha() {}
}