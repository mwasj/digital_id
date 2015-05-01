package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import core.CommandResponse;
import models.Instruction;
import models.WebUpdate;
import play.mvc.*;
import play.libs.*;
import play.libs.F.*;

import akka.actor.*;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static akka.pattern.Patterns.ask;


/**
 * Created by wasinski on 10/04/2015.
 */
public class CaptureUpdater extends UntypedActor
{
    static ActorRef actor = Akka.system().actorOf(Props.create(CaptureUpdater.class));

    Map<String, WebSocket.Out<JsonNode>> members = new HashMap<String, WebSocket.Out<JsonNode>>();

    @Override
    public void onReceive(Object message) throws Exception {
        System.out.println("OnReceive called: ");

        if (message instanceof Join) {
            System.out.println("Join message");
            // Received a Join message
            Join join = (Join) message;

            // Check if this username is free.
            if (members.containsKey(join.username)) {
                getSender().tell("This username is already used", actor);
            } else {
                members.put(join.username, join.channel);
                notifyAll("join", join.username, "has entered the room");
                getSender().tell("OK", actor);
            }
        } else if (message instanceof Talk) {
            System.out.println("Talk message");
            // Received a Talk message
            Talk talk = (Talk) message;

            notifyAll("talk", talk.username, talk.text);
        }
        else if (message instanceof Update)
        {
            Update update = (Update) message;
            sendProgressUpdate(update.username, update.webUpdate);
            System.out.println("update message");
        }

    }

    private void sendAnslysis(String username, String text)
    {
        ObjectNode event = Json.newObject();
        event.put("type", "analysis");
        event.put("content", text);
        members.get(username).write(event);
    }

    private void sendProgressUpdate(String username, WebUpdate webUpdate)
    {
        ObjectNode event = Json.newObject();
        event.put("type", "progressUpdate");
        event.put("content", new Gson().toJson(webUpdate));
        members.get(username).write(event);
    }

    public static void update(String id, WebUpdate webUpdate) throws Exception
    {
        actor.tell(new Update(id, webUpdate), null);
        //String result = (String)  Await.result(ask(actor, new Update(id, infornatmion), 1000), Duration.create(1, TimeUnit.SECONDS));
    }

    /**
     *
     * @param id
     * @param in
     * @param out
     * @throws Exception
     */
    public static void register(final String id, final WebSocket.In<JsonNode> in, final WebSocket.Out<JsonNode> out) throws Exception {
        // Send the Join message to the room
        String result = (String) Await.result(ask(actor, new Join(id, out), 1000), Duration.create(1, TimeUnit.SECONDS));

        if("OK".equals(result)) {

            // For each event received on the socket,
            in.onMessage(new Callback<JsonNode>() {
                public void invoke(JsonNode event) {

                    // Send a Talk message to the room.
                    actor.tell(new Talk(id, event.get("text").asText()),actor);

                }
            });

            // When the socket is closed.
            in.onClose(new Callback0() {
                public void invoke() {

                    // Send a Quit message to the room.
                    actor.tell(new Quit(id),actor);

                }
            });

        } else {

            // Cannot connect, create a Json error.
            ObjectNode error = Json.newObject();
            error.put("error", result);

            // Send the error to the socket.
            out.write(error);

        }
    }

    // Send a Json event to all members
    public void notifyAll(String kind, String user, String text)
    {
        for(WebSocket.Out<JsonNode> channel: members.values())
        {
            System.out.println(kind + " " + user + " " + text);
            ObjectNode event = Json.newObject();
            event.put("kind", kind);
            event.put("user", user);
            event.put("message", text);

            ArrayNode m = event.putArray("members");
            for(String u: members.keySet()) {
                m.add(u);
            }

            channel.write(event);
        }
    }

    public static class RegistrationMessage {
        public String id;
        public WebSocket.Out<JsonNode> channel;

        public RegistrationMessage(String id, WebSocket.Out<JsonNode> channel) {
            super();
            this.id = id;
            this.channel = channel;
        }
    }

    public static class UnregistrationMessage {
        public String id;

        public UnregistrationMessage(String id) {
            super();
            this.id = id;
        }
    }

    public static class MoveMessage {

        public float longitude;

        public float latitude;

        public MoveMessage(float longitude, float latitude) {
            this.longitude = longitude;
            this.latitude = latitude;
        }

    }

    public static class Join {

        final String username;
        final WebSocket.Out<JsonNode> channel;

        public Join(String username, WebSocket.Out<JsonNode> channel) {
            this.username = username;
            this.channel = channel;
        }

    }

    public static class Talk {

        final String username;
        final String text;

        public Talk(String username, String text) {
            this.username = username;
            this.text = text;
        }

    }

    public static class Update {
        final String username;
        final WebUpdate webUpdate;

        public Update(String username, WebUpdate webUpdate) {
            this.username = username;
            this.webUpdate = webUpdate;
        }
    }

    public static class Quit {

        final String username;

        public Quit(String username) {
            this.username = username;
        }

    }
}
