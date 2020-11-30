package com.venosyd.open.mail;

import java.util.Map;

import com.venosyd.open.mail.logic.MailBS;

/**
 * @author sergio lisan <sels@venosyd.com>
 */
public abstract class Mail {

    /**
     * /mail recebe dados no mapa de parametros para montar a mensagem que sera
     * encaminhada ao destinatario
     * 
     * { from: 'fulano@email.com' passwd: ABC3838DE9F73ABC28 fromName: 'Fulano
     * Enterprises' emails: ['ciclano@email.com', 'beltrano@email.com'] title:
     * 'Promocao' payload: '<html>Todos promovidos a sarjeta</html>' }
     */
    public static Map<String, Object> send(Map<String, Object> params) {
        return MailBS.INSTANCE.send(params);
    }
}
