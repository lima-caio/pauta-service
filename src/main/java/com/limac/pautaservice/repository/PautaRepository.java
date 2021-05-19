package com.limac.pautaservice.repository;

import com.limac.pautaservice.domain.Pauta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository para o domínio de {@link Pauta}.
 */
@Repository
public interface PautaRepository extends MongoRepository<Pauta, String> {

}
