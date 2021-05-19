package com.limac.pautaservice.mapping;

import com.limac.pautaservice.domain.Voto;
import com.limac.pautaservice.rest.dto.VotoDto;
import org.mapstruct.Mapper;

/**
 * Mapper para {@link Voto}.
 */
@Mapper(componentModel = "spring")
public interface VotoMapper {

    /**
     * Mapeia os valores de {@link VotoDto} para {@link Voto}.
     *
     * @param votoDto {@link VotoDto}
     * @return {@link Voto}
     */
    Voto votoDtoParaVoto(VotoDto votoDto);
}
