package com.venosyd.open.mail.rest;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import com.venosyd.open.commons.util.JSONUtil;
import com.venosyd.open.commons.util.RESTService;
import com.venosyd.open.mail.Mail;

/**
 * @author sergio lisan <sels@venosyd.com>
 */
@Path("/")
public class MailRSImpl implements MailRS, RESTService {

    @Context
    private HttpHeaders headers;

    public MailRSImpl() {

    }

    @Override
    public Response echo() {
        String message = "MAIL ECHO GRANTED" + Calendar.getInstance().get(Calendar.YEAR);

        var echoMessage = new HashMap<String, String>();
        echoMessage.put("status", "ok");
        echoMessage.put("message", message);

        return makeResponse(echoMessage);
    }

    @Override
    public Response send(String body) {
        Function<Map<String, String>, Response> operation = (request) -> {
            var requisicao = new HashMap<String, Object>();
            requisicao.putAll(request);

            requisicao.put("emails", JSONUtil.fromJSONToList((String) request.get("emails"), String.class));

            requisicao.remove("hash");
            requisicao.remove("token");
            requisicao.remove("database");

            var result = Mail.send(requisicao);

            if (result.get("status").equals("ok")) {
                var response = new HashMap<String, String>();
                response.put("status", "ok");
                response.put("message", "Email enviado com sucesso");

                return makeResponse(response);
            } else {
                return makeErrorResponse("Problemas ao enviar o EMAIL: " + result.get("details"));
            }
        };

        var authorization = getauthcode(headers);
        var arguments = Arrays.<String>asList("emails", "hash");

        return authorization != null ? process(_unwrap(body, true), authorization, arguments, operation) // headers
                : process(_unwrap(body, false), operation); // com token via post
    }

    //
    // PRIVATE METHODS
    //

    private Map<String, String> _unwrap(String body, boolean withouttoken) {
        body = unzip(body);
        var request = JSONUtil.<String, String>fromJSONToMap(body);

        if (request.containsKey("hash")) {
            var hash = (String) request.get("hash");

            String token;
            String database;
            String passwd;

            if (withouttoken) {
                database = hash.substring(0, 32);
                passwd = hash.substring(32);
            } else {
                token = hash.substring(0, 64);
                database = hash.substring(64, 96);
                passwd = hash.substring(96);

                request.put("token", token);
            }

            request.put("database", database);
            request.put("passwd", passwd);
        }

        return request;
    }

}
