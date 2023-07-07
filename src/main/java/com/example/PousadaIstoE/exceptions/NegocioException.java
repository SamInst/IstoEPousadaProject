package br.gov.ma.siisp.msassistencias.exceptions;

public class NegocioException extends RuntimeException {
    public NegocioException(String message) {
        super(message);
    }
}
