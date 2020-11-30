package com.venosyd.open.mail.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.venosyd.open.commons.log.Debuggable;
import com.venosyd.open.commons.util.Config;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;

/**
 * @author sergio lisan <sels@venosyd.com>
 */
public class MailBSImpl implements MailBS, Debuggable {

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> send(Map<String, Object> params) {
        try {
            // faz uma checkagem para ver se todos os campos necessarios para o envio do
            // email de fato estao setados
            List<Boolean> validated = new ArrayList<>();
            List<String> keys = Arrays.asList("from", "passwd", "fromName", "emails", "title", "payload");
            params.keySet().forEach(k -> validated.add(keys.contains(k)));

            // se validar envia o email e alea jacta est
            if (!validated.contains(false)) {
                return _sendMessage((String) params.get("from"), (String) params.get("passwd"),
                        (String) params.get("fromName"), (List<String>) params.get("emails"),
                        (String) params.get("title"), (String) params.get("payload"));
            }

            // erro durante a validacao dos campos
            else {
                var result = new HashMap<String, Object>();
                result.put("status", "error");
                result.put("details", "missing fields");
                result.put("message", "problemas ao enviar o email");

                return result;
            }
        } catch (Exception e) {
            err.exception("MAIL SEND MAIL EXCEPTION", e);

            var result = new HashMap<String, Object>();
            result.put("status", "error");
            result.put("details", e.getMessage());
            result.put("message", "problemas ao enviar o email");

            return result;
        }
    }

    private Map<String, Object> _sendMessage(String from, String passwd, String fromName, List<String> recipients,
            String subject, String email) throws Exception {
        // config
        var mail = Config.INSTANCE.<Map<String, String>>get("mail");
        var htmlEmail = new HtmlEmail();

        var sslport = mail.get("ssl-port");
        var authpasswd = new String(Base64.decodeBase64(passwd));

        htmlEmail.setSmtpPort(Integer.parseInt(sslport));
        htmlEmail.setAuthenticator(new DefaultAuthenticator(from, authpasswd));
        // htmlEmail.setDebug(true);
        htmlEmail.setHostName(mail.get("hostname"));

        var properties = htmlEmail.getMailSession().getProperties();
        properties.put("mail.smtps.auth", "true");
        // properties.put("mail.debug", "true");
        properties.put("mail.smtps.port", sslport);
        properties.put("mail.smtps.socketFactory.port", sslport);
        properties.put("mail.smtps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtps.socketFactory.fallback", "false");
        properties.put("mail.smtp.starttls.enable", "true");

        // email per se
        htmlEmail.setFrom(from, fromName);
        htmlEmail.addTo(recipients.toArray(new String[recipients.size()]));
        htmlEmail.setSubject(subject);
        htmlEmail.setHtmlMsg(email);

        // manda o email
        htmlEmail.send();

        // se nao der certo, o apache-commons-email manda uma excessao e nem passa
        // por aqui
        var result = new HashMap<String, Object>();
        result.put("status", "ok");
        result.put("message", "email enviado com sucesso");

        return result;
    }
}
