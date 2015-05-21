package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.ActionUpdateDto;
import dtos.AnalysisDto;
import dtos.CommandUpdateDto;
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
public class WebUpdateForwarder extends UntypedActor
{
    static ActorRef actor = Akka.system().actorOf(Props.create(WebUpdateForwarder.class));

    Map<String, WebSocket.Out<JsonNode>> members = new HashMap<String, WebSocket.Out<JsonNode>>();

    @Override
    public void onReceive(Object message) throws Exception
    {
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
        }
        else if (message instanceof Talk) {
            System.out.println("Talk message");
            // Received a Talk message
            Talk talk = (Talk) message;

            notifyAll("talk", talk.username, talk.text);
        }
        else if (message instanceof Update)
        {
            Update update = (Update) message;
            String type = "";

            switch(update.webUpdate.getWebUpdateType())
            {
                case finish:
                    type = "finish";
                    break;
                case progressUpdate:
                    type = "progressUpdate";
                    break;
                case analysis:
                    //sendAnalysis(update.username);
                    break;
            }
        }
        else if( message instanceof AnalysisDto)
        {
            AnalysisDto analysisDto = (AnalysisDto) message;
            ObjectNode event = Json.newObject();
            event.put("type", "analysis");
            event.put("content", new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(analysisDto.getActions()));
            members.get(analysisDto.getUserName()).write(event);
        }
        else if( message instanceof ActionUpdateDto)
        {
            ActionUpdateDto actionUpdateDto = (ActionUpdateDto) message;
            ObjectNode event = Json.newObject();
            event.put("type", "action_update");
            event.put("content", new Gson().toJson(actionUpdateDto));
            members.get(actionUpdateDto.getUserName()).write(event);
        }
        else if( message instanceof CommandUpdateDto)
        {
            CommandUpdateDto commandUpdateDto = (CommandUpdateDto) message;
            ObjectNode event = Json.newObject();
            event.put("type", "command_update");
            event.put("content", new Gson().toJson(commandUpdateDto));
            members.get(commandUpdateDto.getUserName()).write(event);
        }
    }

    private void sendUpdate(String username, String content, String type)
    {
        ObjectNode event = Json.newObject();
        event.put("type", type);
        event.put("content", content);
        members.get(username).write(event);
    }

    public static void update(String username, WebUpdate webUpdate) throws Exception
    {
        actor.tell(new Update(username, webUpdate), null);
        //String result = (String)  Await.result(ask(actor, new Update(id, infornatmion), 1000), Duration.create(1, TimeUnit.SECONDS));
    }

    public static void sendAnalysis(AnalysisDto analysisDto)
    {
        actor.tell(analysisDto, null);
    }

    public static void sendActionUpdate(ActionUpdateDto actionUpdateDto)
    {
        actor.tell(actionUpdateDto, null);
    }

    public static void sendCommandUpdate(CommandUpdateDto commandUpdateDto)
    {
        actor.tell(commandUpdateDto, null);
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

    public static class Update
    {
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
