package com.limac.pautaservice.rest.client.dto;

import com.limac.pautaservice.type.CpfStatusType;
import lombok.Data;

/**
 * Objeto de transferência de dados de CPF.
 */
@Data
public class CpfResultadoDto {

    private CpfStatusType status;
}
