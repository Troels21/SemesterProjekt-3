package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class ekgController {

    static private List<Double> measurements;
    static private String cpr;
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


     public void validate(String data, String cprString){
        JsonElement json = new JsonParser().parse(data);

        JsonArray jsonArray = json.getAsJsonArray();

        measurements = new Gson().fromJson(jsonArray, ArrayList.class);
        cpr = cprString;
        System.out.println(measurements.toString());
        System.out.println(measurements.get(3));
        System.out.println(measurements.get(9999));
        System.out.println(cpr);


    }

    private final Thread ekgAlgoThread = new Thread(()-> {

    });

    public void findSick(){



        markers= markers+2;
    }

    public void getXmlEkgSession(){
    }

    public void getXmlMeasurements(){
    }

}
