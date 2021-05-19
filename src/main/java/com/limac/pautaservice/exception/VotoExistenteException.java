package com.limac.pautaservice.exception;

import com.limac.pautaservice.domain.Voto;

/**
 * Exception para quando um {@link Voto} já existe.
 */
public class VotoExistenteException extends RuntimeException {

    public VotoExistenteException(String message) {
        super(message);
    }
}
