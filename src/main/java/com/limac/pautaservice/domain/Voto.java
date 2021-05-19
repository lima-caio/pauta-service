package com.limac.pautaservice.domain;

import com.limac.pautaservice.type.VotoType;
import com.limac.pautaservice.validation.annotation.Cpf;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * Objeto de domínio do Voto.
 */
@Data
@EqualsAndHashCode(exclude = "votoType")
public class Voto {

    @Cpf
    private String cpf;

    @NotNull
    private VotoType votoType;
}
