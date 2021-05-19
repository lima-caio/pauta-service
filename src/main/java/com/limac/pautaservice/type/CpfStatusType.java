package com.limac.pautaservice.type;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Enumeration para status de CPF.
 */
@RequiredArgsConstructor
public enum CpfStatusType {

    /**
     * Status ABLE_TO_VOTE.
     */
    ABLE_TO_VOTE("ABLE_TO_VOTE"),

    /**
     * Status UNABLE_TO_VOTE.
     */
    UNABLE_TO_VOTE("UNABLE_TO_VOTE");

    @Getter
    @JsonValue
    @Accessors(fluent = true)
    private final String cpfStatus;
}
