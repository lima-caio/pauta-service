package com.limac.pautaservice.mapping;

import com.limac.pautaservice.domain.Pauta;
import com.limac.pautaservice.rest.dto.PautaCriacaoDto;
import com.limac.pautaservice.rest.dto.PautaDto;
import com.limac.pautaservice.rest.dto.PautaResultadoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para {@link Pauta}.
 */
@Mapper(componentModel = "spring")
public interface PautaMapper {

    /**
     * Expressão para mapear o target 'minutosDuracao'.
     */
    String TEMPO_DURACAO_EXPRESSION =
        "java(pautaCriacaoDto.getTempoDuracao() == null || pautaCriacaoDto.getTempoDuracao() <= 0 ? 1 : pautaCriacaoDto.getTempoDuracao())";

    /**
     * Mapeia os valores de {@link PautaCriacaoDto} para {@link Pauta}.
     *
     * @param pautaCriacaoDto {@link PautaCriacaoDto}
     * @return {@link Pauta}
     */
    @Mapping(target = "tempoDuracao", expression = TEMPO_DURACAO_EXPRESSION)
    Pauta pautaCriacaoDtoParaPauta(PautaCriacaoDto pautaCriacaoDto);

    /**
     * Mapeia os valores de {@link Pauta} para {@link PautaDto}.
     *
     * @param pauta {@link Pauta}
     * @return {@link PautaDto}
     */
    PautaDto pautaParaPautaDto(Pauta pauta);

    /**
     * Mapeia os valores de {@link Pauta} para {@link PautaResultadoDto}.
     *
     * @param pauta {@link Pauta}
     * @return {@link PautaResultadoDto}
     */
    PautaResultadoDto pautaParaPautaResultadoDto(Pauta pauta);
}

