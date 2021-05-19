package com.limac.pautaservice.exception;

import com.limac.pautaservice.domain.Pauta;

/**
 * Exception para quando uma {@link Pauta} está fechada.
 */
public class PautaFechadaException extends RuntimeException {

    public PautaFechadaException(String message) {
        super(message);
    }
}
