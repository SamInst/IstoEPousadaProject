package com.example.PousadaIstoE.exceptions.handler;

import com.example.PousadaIstoE.exceptions.EntityConflict;
import com.example.PousadaIstoE.exceptions.EntityNotFound;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ApiExceptionHandler {
    private static final String ERROR_BUSINESS = "Erro de Negócio";
    private static final String ENTIDADE_NAO_ENCONTRADA = "Entidade Não encontrada";
    private static final String ERRROR_REQUEST = "Erro de Requisição";
    private static final String ERRROR_SERVER = "Erro de Servidor";

    private static final String MSG_ERRO_SERVER = "Ocorreu um erro inesperado, entre em contato com administrador do sistema";



    @Value("${spring.application.name}")
    private String projetoName;

    private final MessageSource messageSource;

    public ApiExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(EntityConflict.class)
    public ResponseEntity<ErrorMessage> handleNegocioException(EntityConflict ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(
                        projetoName,
                        request.getRequestURI(),
                        ERROR_BUSINESS,
                        ex.getMessage(),
                        ex.getClass().getName()));
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorMessage> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(
                        projetoName,
                        request.getRequestURI(),
                        ERRROR_REQUEST,
                        ex.getMessage(),
                        ex.getClass().getName()));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageValidacao> handleMethodNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessageValidacao(
                        projetoName,
                        request.getRequestURI(),
                        ERRROR_REQUEST,
                        ex.getObjectName(),
                        ex.getClass().getName(),
                        criaErrosValidacaoPorCampo(ex)));
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ErrorMessageValidacao> handleInvalidFormat(InvalidFormatException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessageValidacao(
                        projetoName,
                        request.getRequestURI(),
                        ERRROR_REQUEST,
                        ex.getOriginalMessage(),
                        ex.getClass().getName(),
                        criarErrosValidacaoPorTipo(ex)));
    }

    @ExceptionHandler(EntityNotFound.class)
    public ResponseEntity<ErrorMessage> handleEntidadeNaoEncontrada(EntityNotFound ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(
                        projetoName,
                        request.getRequestURI(),
                        ENTIDADE_NAO_ENCONTRADA,
                        ex.getMessage(),
                        ex.getClass().getName()));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleEntidade(Exception ex, HttpServletRequest request) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(
                        projetoName,
                        request.getRequestURI(),
                        ERRROR_SERVER,
                        MSG_ERRO_SERVER,
                        ex.getClass().getName()));
    }
    private List<ErroDeValidacao> criaErrosValidacaoPorCampo(MethodArgumentNotValidException ex){
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<ErroDeValidacao> erros = new ArrayList<>();
        fieldErrors.forEach(e -> {
            String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
            erros.add(new ErroDeValidacao(e.getField(), mensagem));
        });
        return erros;
    }
    private List<ErroDeValidacao> criarErrosValidacaoPorTipo(InvalidFormatException ex){
        List<ErroDeValidacao> erros = new ArrayList<>();
        ex.getPath().forEach(e -> {
            String mensagem = String.format("Valor inválido para o campo '%s'. Esperado tipo %s.",
                    e.getFieldName(), ex.getTargetType().getSimpleName());
            ErroDeValidacao erro = new ErroDeValidacao(e.getFieldName(), mensagem);
            erros.add(erro );
        });
        return erros;
    }
}
