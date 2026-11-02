package br.edu.insper.projeto_final_objetos.common;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    public static class ErroResponse {
        private int status;
        private String message;
        private List<String> errors;

        public ErroResponse() { }

        public ErroResponse(int status, String message, List<String> errors) {
            this.status = status;
            this.message = message;
            this.errors = errors;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<String> getErrors() {
            return errors;
        }

        public void setErrors(List<String> errors) {
            this.errors = errors;
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> tratarValidacao(MethodArgumentNotValidException ex) {
        var erros = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage()).toList();
        return ResponseEntity.badRequest().body(new ErroResponse(400, "Erro de validação", erros));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErroResponse> tratarRegra(ResponseStatusException ex) {
        String mensagem = ex.getReason() == null ? "Erro na requisição" : ex.getReason();
        return ResponseEntity.status(ex.getStatusCode())
                .body(new ErroResponse(ex.getStatusCode().value(), mensagem, List.of()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> tratarJson() {
        return ResponseEntity.badRequest().body(new ErroResponse(400,
                "JSON inválido ou campo com tipo incorreto", List.of()));
    }
}
