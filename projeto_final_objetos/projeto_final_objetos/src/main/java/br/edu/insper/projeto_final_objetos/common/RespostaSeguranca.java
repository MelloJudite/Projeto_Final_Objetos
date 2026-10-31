package br.edu.insper.projeto_final_objetos.common;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

final class RespostaSeguranca {
    private RespostaSeguranca() { }

    // Recebe somente as mensagens constantes de segurança desta aplicação.
    static void escrever(HttpServletResponse response, int status, String mensagem) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"status\":" + status + ",\"message\":\"" + mensagem + "\",\"errors\":[]}");
    }
}
