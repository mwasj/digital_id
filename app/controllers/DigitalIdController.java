package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import core.DigitalID;
import core.DigitalIDUtils;
import core.DigitalIdComparator;
import core.HtmlGenerator;
import dtos.ComparisonDTO;
import models.WebUpdate;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import runners.DigitalIdRunner;

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
        DigitalIdRunner runner = new DigitalIdRunner(request().body().asJson().toString(), sessionName);
        runner.initialise();
        runner.sendAnalysis();
        runner.start();
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

        ComparisonDTO comparisonDTO = new Gson().fromJson(request().body().asJson().toString(), new TypeToken<ComparisonDTO>() {}.getType());

        DigitalIdComparator comparator = new DigitalIdComparator(comparisonDTO);

        return ok(new Gson().toJson(comparator.performComparison()));
    }

    public static Result download(String filename)
    {
        System.out.println("download called: " + filename);
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
                    WebUpdateForwarder.register(username, in, out);
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
            WebUpdateForwarder.update(username, webUpdate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Result generateDigitalIdReport()
    {
        System.out.println("Called");
        ArrayList<String> xml = new Gson().fromJson(request().body().asJson().toString(), new TypeToken<ArrayList<String>>() {}.getType());
        System.out.println("getDigitalIdReport called with: " + xml.get(0));
        DigitalID digitalId = DigitalIDUtils.unMarshall("C:\\digital_ids\\"+xml.get(0));
        DigitalIdComparator comparator = new DigitalIdComparator(digitalId, null);
        comparator.generateNonComparisonReport();

        HtmlGenerator generator = new HtmlGenerator(comparator.getAccordions(), comparator.getContentDtos());

        String html = "";

        try {
            html = generator.generateReportBackbone();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return ok(html);
    }
}