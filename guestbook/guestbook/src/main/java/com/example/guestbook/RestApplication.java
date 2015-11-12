package com.example.guestbook;

/**
 * Created by Vladi on 04/11/15.
 */
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.routing.Router;

import java.util.List;

public class RestApplication extends Application {

    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());

        router.attach("/guestbook/", new Restlet() {
            @Override
            public void handle(Request request, Response response) {
                List<Greeting> greetings = ObjectifyService.ofy().load().type(Greeting.class).order("-date").list();
                XMLStringBuilder sb = new XMLStringBuilder();
                sb.openTag("guestbook", true);
                for (Greeting greeting : greetings) {
                    sb.appendParagraph(greeting.toXML());
                }
                sb.closeTag();
                response.setEntity(sb.toString(), MediaType.TEXT_PLAIN);
            }
        });

        router.attach("/guestbook/{greeting_id}", new Restlet() {
            @Override
            public void handle(Request request, Response response) {
                Long greetingID = Long.parseLong((String)request.getAttributes().get("greeting_id"));
                Greeting greeting = ObjectifyService.ofy().load().key(Key.create(Key.create(Guestbook.class, "default"), Greeting.class, greetingID)).now();
                String resp = "Greeting with id " + greetingID + " not found.";
                if (greeting != null)
                    resp = greeting.toXML();
                response.setEntity(resp, MediaType.TEXT_PLAIN);
            }
        });

        return router;
    }
}
