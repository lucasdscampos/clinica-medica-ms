package br.edu.imepac.agendamento.exception;

import br.edu.imepac.comum.exceptions.NotFoundClinicaMedicaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ClinicaMedicaHandleExceptions {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("Um erro inesperado ocorreu: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado, tente novamente!");
    }

    @ExceptionHandler(AuthenticationClinicaMedicaException.class)
    public ResponseEntity<String> handleUnauthorized(AuthenticationClinicaMedicaException e) {
        log.error("Ocorreu um erro de autenticação/autorização: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Dados de acesso inválido!");
    }

    @ExceptionHandler(NotFoundClinicaMedicaException.class)
    public ResponseEntity<String> handleNotFound(NotFoundClinicaMedicaException e) {
        log.error("Recurso não encontrado: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}