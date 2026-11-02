package br.edu.insper.projeto_final_objetos.servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Integer> {
    @Query("SELECT s FROM Servico s WHERE TYPE(s) = :tipo")
    List<Servico> buscarPorTipo(
            @Param("tipo") Class<? extends Servico> tipo
    );
}