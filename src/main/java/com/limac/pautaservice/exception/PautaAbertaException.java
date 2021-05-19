package com.limac.pautaservice.exception;

import com.limac.pautaservice.domain.Pauta;

/**
 * Exception para quando uma {@link Pauta} ainda está aberta.
 */
public class PautaAbertaException extends RuntimeException {

    public PautaAbertaException(String message) {
        super(message);
    }
}
