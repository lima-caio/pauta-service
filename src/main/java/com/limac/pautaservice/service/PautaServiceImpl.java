package com.limac.pautaservice.service;

import com.limac.pautaservice.domain.Pauta;
import com.limac.pautaservice.domain.Voto;
import com.limac.pautaservice.exception.CpfInvalidoException;
import com.limac.pautaservice.exception.NaoEncontradoException;
import com.limac.pautaservice.exception.PautaAbertaException;
import com.limac.pautaservice.exception.PautaFechadaException;
import com.limac.pautaservice.exception.VotoExistenteException;
import com.limac.pautaservice.messaging.publisher.PautaPublisher;
import com.limac.pautaservice.repository.PautaRepository;
import com.limac.pautaservice.service.runnable.FecharPautaRunnable;
import com.limac.pautaservice.type.ResultadoType;
import com.limac.pautaservice.type.VotoType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

/**
 * Implementação do Serviço para executar operações de {@link Pauta}.
 */
@Service
@RequiredArgsConstructor
public class PautaServiceImpl implements PautaService {

    private static final String PAUTA_NAO_ENCONTRADA = "Pauta '%s' não foi encontrada";

    private final CpfService cpfService;
    private final PautaPublisher pautaPublisher;
    private final PautaRepository pautaRepository;
    private final AgendadorService agendadorService;

    /**
     * {@inheritDoc}
     */
    @Override
    public Pauta criarPauta(Pauta pauta) {
        return pautaRepository.insert(pauta);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pauta procurarPauta(String pautaId) {
        return pautaRepository.findById(Objects.requireNonNull(pautaId)).orElseThrow(() -> {
            throw new NaoEncontradoException(String.format(PAUTA_NAO_ENCONTRADA, pautaId));
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void abrirPauta(String pautaId) {
        final Pauta pauta = procurarPauta(pautaId);

        if (pauta.isAberta()) {
            throw new PautaAbertaException(String.format("Pauta '%s' já está aberta", pautaId));
        }

        pauta.setAberta(true);

        pautaRepository.save(pauta);

        agendadorService.agendarExecucao(new FecharPautaRunnable(pautaId, this), pauta.getTempoDuracao(), pauta.getTempoUnidade());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fecharPauta(String pautaId) {
        final Pauta pauta = procurarPauta(pautaId);

        pauta.setAberta(false);
        definirResultado(pauta);

        pautaRepository.save(pauta);

        pautaPublisher.publicarResultado(pauta);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void adicionarVoto(String pautaId, Voto voto) {
        final Pauta pauta = procurarPauta(pautaId);

        if (!pauta.isAberta()) {
            throw new PautaFechadaException(String.format("Pauta '%s' está fechada", pautaId));
        }

        if (!cpfService.isValido(voto.getCpf())) {
            throw new CpfInvalidoException(String.format("Associado '%s' não pode votar", voto.getCpf()));
        }

        adicionarVoto(pauta, voto);

        pautaRepository.save(pauta);
    }

    private void adicionarVoto(Pauta pauta, Voto voto) {
        final Set<Voto> votos = pauta.getVotos();
        if (votos.contains(voto)) {
            throw new VotoExistenteException(String.format("Voto do Associado '%s' já existe", voto.getCpf()));
        }

        if (VotoType.SIM.equals(voto.getVotoType())) {
            pauta.setSim(pauta.getSim() + 1);
        } else {
            pauta.setNao(pauta.getNao() + 1);
        }

        votos.add(voto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pauta buscarResultado(String pautaId) {
        final Pauta pauta = procurarPauta(pautaId);

        definirResultado(pauta);

        return pauta;
    }

    private void definirResultado(Pauta pauta) {
        ResultadoType resultado = ResultadoType.EMPATE;
        if (pauta.getSim() > pauta.getNao()) {
            resultado = ResultadoType.SIM;
        } else if (pauta.getSim() < pauta.getNao()) {
            resultado = ResultadoType.NAO;
        }

        pauta.setResultado(resultado);
    }
}
