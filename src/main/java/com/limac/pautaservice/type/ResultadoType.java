package com.limac.pautaservice.type;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Enumeration para tipos de resultados.
 */
@RequiredArgsConstructor
public enum ResultadoType {

    /**
     * Tipo de resultado que o SIM venceu.
     */
    SIM("SIM"),

    /**
     * Tipo de resultado que o NÃO venceu.
     */
    NAO("NAO"),

    /**
     * Tipo de resultado que empatou.
     */
    EMPATE("EMPATE");

    @Getter
    @JsonValue
    @Accessors(fluent = true)
    private final String resultado;

    @Override
    public String toString() {
        return this.resultado;
    }
}
