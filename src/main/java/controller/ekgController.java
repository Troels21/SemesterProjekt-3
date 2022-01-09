package controller;

import java.util.ArrayList;
import java.util.List;

public class ekgController {

    private List<Double> measurements;
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

    public static void main(String[] args) {
        ArrayList<Double> listen = ekgController.splitStringArray(",1,2,3,4,5,6,7,8,9,23,32,41,252,3,562,34,421,3,0");
        System.out.println(CPR);
        System.out.println(listen.toString());
    }

    public void validate(){

    }

    private final Thread ekgAlgoThread = new Thread(()-> {

    });

    public void findSick(List<Double> ekgList){
        markers= markers+2;
    }

    public void getXmlEkgSession(){
    }

    public void getXmlMeasurements(){
    }

    static public ArrayList<Double> splitStringArray(String string) {
        CPR= string.substring(1);
        String[] Stringarray = string.split(",");
        for (int i = 1; i < Stringarray.length; i++) {
            liste.add(Double.parseDouble(Stringarray[i]));

        }
        return liste;
    }
}
