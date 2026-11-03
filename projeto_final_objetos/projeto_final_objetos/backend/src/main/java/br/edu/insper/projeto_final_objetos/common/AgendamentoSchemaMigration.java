package br.edu.insper.projeto_final_objetos.common;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class AgendamentoSchemaMigration implements ApplicationRunner {
    private final JdbcTemplate jdbc;

    public AgendamentoSchemaMigration(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void run(ApplicationArguments args) {
        // Pedidos pendentes ainda não têm profissional atribuído.
        try {
            jdbc.execute("ALTER TABLE AGENDAMENTO ALTER COLUMN ID_PROFISSIONAL SET NULL");
        } catch (Exception ignored) {
            // Em bancos novos a coluna já nasce anulável.
        }
        // A unicidade agora é verificada por profissional durante a aceitação,
        // permitindo que o horário seja reutilizado após um cancelamento.
        try {
            jdbc.execute("ALTER TABLE AGENDAMENTO DROP CONSTRAINT IF EXISTS UK_PROFISSIONAL_HORARIO");
        } catch (Exception ignored) {
            // A constraint pode não existir em instalações novas.
        }
        try {
            jdbc.update("UPDATE AGENDAMENTO SET STATUS = 'ACEITO' WHERE STATUS IS NULL AND ID_PROFISSIONAL IS NOT NULL");
            jdbc.update("UPDATE AGENDAMENTO SET STATUS = 'PENDENTE' WHERE STATUS IS NULL AND ID_PROFISSIONAL IS NULL");
        } catch (Exception ignored) {
            // A tabela/status pode não existir em bases antigas ainda não migradas.
        }
    }
}
