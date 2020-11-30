package com.venosyd.open.mail.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author sergio lisan <sels@venosyd.com>
 */
public interface MailRS {

    /** */
    String MAIL_BASE_URI = "/mail";

    /** */
    String MAIL_ECHO = "/echo";

    /** */
    String MAIL_SEND = "/send";

    /**
     * Hello from the server siiiiiiide!
     */
    @GET
    @Path(MAIL_ECHO)
    @Produces({ MediaType.APPLICATION_JSON })
    Response echo();

    /**
     * retorna um logradouro com base no CEP (zipCode)
     * 
     * { from: 'fulano@email.com' passwd: ABC3838DE9F73ABC28 fromName: 'Fulano
     * Enterprises' emails: ['ciclano@email.com', 'beltrano@email.com'] title:
     * 'Promocao' payload: '<html>Todos promovidos a sarjeta</html>' }
     */
    @POST
    @Path(MAIL_SEND)
    @Produces({ MediaType.APPLICATION_JSON })
    Response send(String body);

}
