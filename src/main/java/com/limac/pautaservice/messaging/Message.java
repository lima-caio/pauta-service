package com.limac.pautaservice.messaging;

/**
 * Interface de mensagem.
 *
 * @param <P> tipo do payload.
 */
public interface Message<P> {

    String getMessageId();

    P getPayload();
}
