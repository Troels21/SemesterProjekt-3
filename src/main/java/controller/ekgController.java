package controller;

import com.google.gson.*;
import dataAccesLayer.EkgSql;
import dataAccesLayer.apiDAO;
import model.ekgMeasurements;
import model.ekgSession;
import model.ekgSessionList;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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
                markers = (markers + markerToDistance.get(i) + ",");
            }
        }

        try {
            EkgSql.getEkgSql().sqlInsertEkgSession(markers, "BPM: " + bpm + "", cpr);

            EkgSql.getEkgSql().sqlInsertEkgMeasurements(cpr, measurements);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    });


    public ekgSessionList getAllSessions(String CPR) throws SQLException {
        ekgSessionList ekgses = EkgSql.getEkgSql().getEkgSessions(CPR);
        /*
        JSONObject grp1 = apiDAO.getApiDAOOBJ().getJsonOBJ("http://localhost:8080/SemesterProjekt_3_war/data/ekgSessions?cpr=" + CPR);
        for (int i = 0; i < grp1.getJSONObject("sessions").getJSONArray("ekgSession").length(); i++) {
            ekgSession ekgSession = new Gson().fromJson(grp1.getJSONObject("sessions").getJSONArray("ekgSession").get(i).toString(), ekgSession.class);
            ekgSession.setKlinikID(1);
            ekgses.addEkgSession(ekgSession);
        }
        JSONObject grp2 = apiDAO.getApiDAOOBJ().getJsonOBJ("http://localhost:8080/SemesterProjekt_3_war/data/ekgSessions?cpr=" + CPR);
        for (int i = 0; i < grp1.getJSONObject("sessions").getJSONArray("ekgSession").length(); i++) {
            ekgSession ekgSession = new Gson().fromJson(grp1.getJSONObject("sessions").getJSONArray("ekgSession").get(i).toString(), ekgSession.class);
            ekgSession.setKlinikID(2);
            ekgses.addEkgSession(ekgSession);
        }
        JSONObject grp4 = apiDAO.getApiDAOOBJ().getJsonOBJ("http://localhost:8080/SemesterProjekt_3_war/data/ekgSessions?cpr=" + CPR);
        for (int i = 0; i < grp1.getJSONObject("sessions").getJSONArray("ekgSession").length(); i++) {
            ekgSession ekgSession = new Gson().fromJson(grp1.getJSONObject("sessions").getJSONArray("ekgSession").get(i).toString(), ekgSession.class);
            ekgSession.setKlinikID(4);
            ekgses.addEkgSession(ekgSession);
        }
        JSONObject grp5 = apiDAO.getApiDAOOBJ().getJsonOBJ("http://localhost:8080/SemesterProjekt_3_war/data/ekgSessions?cpr=" + CPR);
        for (int i = 0; i < grp1.getJSONObject("sessions").getJSONArray("ekgSession").length(); i++) {
            ekgSession ekgSession = new Gson().fromJson(grp1.getJSONObject("sessions").getJSONArray("ekgSession").get(i).toString(), ekgSession.class);
            ekgSession.setKlinikID(5);
            ekgses.addEkgSession(ekgSession);
        }*/
        return ekgses;
    }


    public ekgMeasurements getAllEKGMeasurements(Integer sessionID, Integer klinikID) throws SQLException {
        ekgMeasurements ekgmeas = new ekgMeasurements();

        switch (klinikID) {
            case 3: {
                ekgMeasurements ekgmeas1 = EkgSql.getEkgSql().getMeasurements(sessionID);
                return ekgmeas1;
            }/*
            case 1: {
                JSONObject grp1 = apiDAO.getApiDAOOBJ().getJsonOBJ("http://localhost:8080/SemesterProjekt_3_war/data/ekgSessions/measurements?sessionID=" + sessionID);
                for (int i = 0; i < grp1.getJSONObject("measurements").getJSONArray("measurment").length(); i++) {
                    ekgmeas.addMeasurments(grp1.getJSONObject("measurements").getJSONArray("measurment").getDouble(i));
                }
                return ekgmeas;
            }
            case 2: {
                JSONObject grp2 = apiDAO.getApiDAOOBJ().getJsonOBJ("http://localhost:8080/SemesterProjekt_3_war/data/ekgSessions/measurements?sessionID=" + sessionID);
                for (int i = 0; i < grp2.getJSONObject("measurements").getJSONArray("measurment").length(); i++) {
                    ekgmeas.addMeasurments(grp2.getJSONObject("measurements").getJSONArray("measurment").getDouble(i));
                }
                return ekgmeas;

            }

            case 4: {
                JSONObject grp4 = apiDAO.getApiDAOOBJ().getJsonOBJ("http://localhost:8080/SemesterProjekt_3_war/data/ekgSessions/measurements?sessionID=" + sessionID);
                for (int i = 0; i < grp4.getJSONObject("measurements").getJSONArray("measurment").length(); i++) {
                    ekgmeas.addMeasurments(grp4.getJSONObject("measurements").getJSONArray("measurment").getDouble(i));
                }
                return ekgmeas;

            }
            case 5: {
                JSONObject grp5 = apiDAO.getApiDAOOBJ().getJsonOBJ("http://localhost:8080/SemesterProjekt_3_war/data/ekgSessions/measurements?sessionID=" + sessionID);
                for (int i = 0; i < grp5.getJSONObject("measurements").getJSONArray("measurment").length(); i++) {
                    ekgmeas.addMeasurments(grp5.getJSONObject("measurements").getJSONArray("measurment").getDouble(i));
                }
                return ekgmeas;
            }*/
        }
        return null;
    }
}
