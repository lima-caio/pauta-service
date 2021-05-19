package com.limac.pautaservice.type;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Enumeration para tipos de votos.
 */
@RequiredArgsConstructor
public enum VotoType {

    /**
     * Tipo de voto SIM.
     */
    SIM("SIM"),

    /**
     * Tipo de voto NÃO.
     */
    NAO("NAO");

    @Getter
    @JsonValue
    @Accessors(fluent = true)
    private final String voto;

    @Override
    public String toString() {
        return this.voto;
    }
}
