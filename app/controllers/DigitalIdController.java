package controllers;

import async.DigitalIdBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import core.DigitalID;
import core.DigitalIDUtils;
import core.DigitalIdComparator;
import core.HtmlGenerator;
import models.WebUpdate;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

    public static Result render()
    {
        return redirect("/");
    }

    public static Result list()
    {

        return ok(new Gson().toJson(DigitalIDUtils.list()));
    }

    public static Result compare()
    {
        System.out.println("Compare called with: " + request().body().asJson().toString());
        ArrayList<String> xmls = new Gson().fromJson(request().body().asJson().toString(), new TypeToken<ArrayList<String>>() {
        }.getType());
        DigitalID digitalID1 = DigitalIDUtils.unMarshall("C:\\digital_ids\\"+xmls.get(0));
        DigitalID digitalID2 = DigitalIDUtils.unMarshall("C:\\digital_ids\\"+xmls.get(1));
        DigitalIdComparator comparator = new DigitalIdComparator(digitalID1, digitalID2);
        comparator.compareHosts();
        HtmlGenerator generator = new HtmlGenerator(comparator.getAccordions());
        String html = "";
        try {
            generator.generateHtml("C:\\digital_ids\\html\\"+digitalID1.getName()+"_"+digitalID2.getName()+".html");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ok();
    }

    public static Result download(String filename)
    {
        System.out.println("download called");
        response().setContentType("application/x-download");
        response().setHeader("Content-disposition","attachment; filename="+filename);
        return ok(new File("C:\\digital_ids\\"+filename));
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