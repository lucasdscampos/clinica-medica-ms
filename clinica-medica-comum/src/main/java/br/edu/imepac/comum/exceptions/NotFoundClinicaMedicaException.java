package br.edu.imepac.comum.exceptions;

public class NotFoundClinicaMedicaException extends RuntimeException {
    public NotFoundClinicaMedicaException(String string) {
        super(string);
    }
}