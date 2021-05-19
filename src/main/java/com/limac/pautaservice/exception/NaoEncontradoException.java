package com.limac.pautaservice.exception;

/**
 * Exception para quando uma entidade não é encontrada.
 */
public class NaoEncontradoException extends RuntimeException {

    public NaoEncontradoException(String message) {
        super(message);
    }
}
