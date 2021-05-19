package com.limac.pautaservice.exception;

/**
 * Exception para quando um CPF é inválido.
 */
public class CpfInvalidoException extends RuntimeException {

    public CpfInvalidoException(String message) {
        super(message);
    }

    public CpfInvalidoException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
