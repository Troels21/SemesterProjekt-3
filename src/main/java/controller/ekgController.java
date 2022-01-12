package controller;

import com.google.gson.*;
import dataAccesLayer.EkgSql;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;


public class ekgController {

    private double[] measurements;
    private String cpr;
    private String markers;
    private String comments;
    private String timeStart;

    static ArrayList<Double> liste;
    static String CPR;

    private ekgController() {
    }

    static private final ekgController EKG_CONTROLLER = new ekgController();

    static public ekgController getEkgController() {
        return EKG_CONTROLLER;
    }

    public void validate(String data, String cprString) {
        try {
            JsonElement json = new JsonParser().parse(data);

            JsonArray jsonArray = json.getAsJsonArray();

            measurements = null;
            measurements = new Gson().fromJson(jsonArray, double[].class);
            cpr = cprString;

            Thread thread = new Thread(findSick);
            thread.start();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private final Thread findSick = new Thread(() -> {
        int lastCounter = -101;
        List<Integer> distance = new ArrayList<>();
        List<Integer> markerToDistance = new ArrayList<>();
        double thisPersonAverageDistance = 0;
        int normalPersonAverageDistance = 500;
        String markers = "";

        for (int i = 0; i < measurements.length; i++) {
            if (measurements[i] >= (Arrays.stream(measurements).max().getAsDouble() * 0.55) & (lastCounter - i < -100)) {
                distance.add((i - lastCounter));
                markerToDistance.add(i);
                lastCounter = i;
            }
        }

        for (int i = 0; i < distance.size(); i++) {
            thisPersonAverageDistance += distance.get(i);
        }

        thisPersonAverageDistance = (thisPersonAverageDistance / distance.size());
        double bpm = (30000 / thisPersonAverageDistance);

        for (int i = 0; i < distance.size(); i++) {
            if ((normalPersonAverageDistance - distance.get(i)) < -500 || (normalPersonAverageDistance - distance.get(i)) > 300) {
                markers = (markers + markerToDistance.get(i) + "|");
            }
        }

        try {
            EkgSql.getEkgSql().sqlInsertEkgSession(markers,"BPM: "+bpm+"",cpr);

            EkgSql.getEkgSql().sqlInsertEkgMeasurements(cpr,measurements);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    });

}
