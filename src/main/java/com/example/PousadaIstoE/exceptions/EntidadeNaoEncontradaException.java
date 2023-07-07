package br.gov.ma.siisp.msassistencias.exceptions;

public class EntidadeNaoEncontradaException extends NegocioException{
    private static final long serialVersionUID = 1L;

    public EntidadeNaoEncontradaException(String mensagem){
        super(mensagem);
    }
}
