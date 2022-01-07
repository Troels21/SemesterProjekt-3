package controller;

import java.util.ArrayList;

public class ekgController {
    static ArrayList<Integer> liste;
    static String CPR;

    private ekgController() {
    }

    static private final ekgController EKG_CONTROLLER = new ekgController();

    static public ekgController getEkgController() {
        return EKG_CONTROLLER;
    }

    public static void main(String[] args) {
        ArrayList<Integer> listen = ekgController.splitStringArray(",1,2,3,4,5,6,7,8,9,23,32,41,252,3,562,34,421,3,0]");
        System.out.println(CPR);
        System.out.println(listen.toString());
    }

    static public ArrayList<Integer> splitStringArray(String string) {
        CPR= string.substring(1);
        String[] Stringarray = string.split(",");
        for (int i = 1; i < Stringarray.length; i++) {
            liste.add(Integer.parseInt(Stringarray[i]));

        }
        return liste;
    }
}
