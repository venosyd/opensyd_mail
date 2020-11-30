package com.venosyd.open.mail.logic;

import java.util.Map;

/**
 * @author sergio lisan <sels@venosyd.com>
 */
public interface MailBS {

    /**
     * singleton
     */
    MailBS INSTANCE = new MailBSImpl();

    /**
     * /mail recebe dados no mapa de parametros para montar a mensagem que sera
     * encaminhada ao destinatario
     * 
     * { from: 'fulano@email.com' passwd: ABC3838DE9F73ABC28 fromName: 'Fulano
     * Enterprises' emails: ['ciclano@email.com', 'beltrano@email.com'] title:
     * 'Promocao' payload: '<html>Todos promovidos a sarjeta</html>' }
     */
    Map<String, Object> send(Map<String, Object> params);
}
