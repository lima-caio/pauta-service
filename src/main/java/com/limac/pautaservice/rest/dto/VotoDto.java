package com.limac.pautaservice.rest.dto;

import com.limac.pautaservice.domain.Voto;
import com.limac.pautaservice.type.VotoType;
import com.limac.pautaservice.validation.annotation.Cpf;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Objeto de transferência de dados de {@link Voto}.
 */
@Data
public class VotoDto {

    @Cpf
    @ApiModelProperty(value = "cpf", required = true, position = 1)
    private String cpf;

    @NotNull(message = "não pode ser null")
    @ApiModelProperty(value = "voto", required = true, position = 2, allowableValues = "SIM, NAO")
    private VotoType votoType;
}
