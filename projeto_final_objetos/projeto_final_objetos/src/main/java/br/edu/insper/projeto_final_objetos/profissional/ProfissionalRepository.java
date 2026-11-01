package br.edu.insper.projeto_final_objetos.profissional;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProfissionalRepository extends JpaRepository<Profissional, Integer> {
    Optional<Profissional> findByEmail(String email);
    boolean existsByEmail(String email);
}
