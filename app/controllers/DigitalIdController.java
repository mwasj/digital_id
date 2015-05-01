package controllers;

import async.DigitalIdBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import core.CommandResponse;
import models.WebUpdate;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;

/**
 * Created by wasinski on 08/04/2015.
 */
public class DigitalIdController extends Controller
{
    public static Result build(final String sessionName)
    {
        DigitalIdBuilder builder = new DigitalIdBuilder(request().body().asJson().toString(), sessionName);
        builder.start();
        return ok();
    }

    public static WebSocket<JsonNode> socketUpdater(final String username) {
        return new WebSocket<JsonNode>() {

            // Called when the Websocket Handshake is done.
            public void onReady(WebSocket.In<JsonNode> in, WebSocket.Out<JsonNode> out){

                // Join the socketUpdater room.
                try {
                    System.out.println("Chat called: " + username);
                    CaptureUpdater.register(username, in, out);
                } catch (Exception ex)
                {
                    System.out.println(ex);
                    ex.printStackTrace();
                }
            }
        };
    }

    public static void updateWebInterface(String username, WebUpdate webUpdate)
    {
        try {
            CaptureUpdater.update(username, webUpdate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}