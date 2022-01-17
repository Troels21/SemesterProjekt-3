package controller;


import com.google.gson.Gson;
import dataAccesLayer.AftaleSQL;
import dataAccesLayer.apiDAO;
import exceptions.OurException;
import model.Aftale;
import model.AftaleListe;
import org.json.JSONObject;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AftaleController {

    private AftaleController() {
    }

    static private final AftaleController AFTALE_CONTROLLER_OBJ = new AftaleController();

    static public AftaleController getAftaleControllerOBJ() {
        return AFTALE_CONTROLLER_OBJ;
    }

    // bolsk værdi til kontrol af cpr'er
    public boolean cprCheck(String name) {
        try {
            return name.length() == 10;
        } catch (Exception e) {
            return false;
        }
    }

    public AftaleListe cprSearch(String cpr) throws SQLException, OurException {
        if (cpr == null) {
            return AftaleSQL.getAftaleSQLObj().getAftalerListe();
        }
        if (cprCheck(cpr)) {
            return AftaleSQL.getAftaleSQLObj().cprSearch(cpr);
        }
        return new AftaleListe();
    }


    public String createAftale(String cpr, String timestart, String timeend, String note) throws OurException {
        Aftale aftale = new Aftale();
        if (cprCheck(cpr)) {
            if (note.length() < 255) {
                aftale.setCPR(cpr);
                aftale.setTimeStart(timestart);
                aftale.setTimeEnd(timeend);
                aftale.setNotat(note);
                aftale.setKlinikID("3");

                AftaleSQL.getAftaleSQLObj().insertAftaleSQL(aftale);
                return "added patient" + aftale;
            } else {
                //forkert note
                OurException ex = new OurException();
                ex.setMessage("For lang note, skal være under 255 tegn.");
                throw ex;
            }
        } else {
            // forkert cpr
            OurException ex = new OurException();
            ex.setMessage("CPR skal være 10 cifre, yyyymmddxxxx");
            throw ex;
        }
    }


    public AftaleListe getAllGroupsAftaleFromTo(String from, String to) throws SQLException {
        AftaleListe aftaleListe = AftaleSQL.getAftaleSQLObj().getAftaleListeDateTime(from, to);
        /*
        JSONObject grp1 = apiDAO.getApiDAOOBJ().getJsonOBJ("http://ekg2.diplomportal.dk:8080/data/aftaler",System.getenv("ApiKeyGrp1"));
        for (int i = 0; i < grp1.getJSONObject("aftaleListe").getJSONArray("aftale").length(); i++) {
            String dato = grp1.getJSONObject("aftaleListe").getJSONArray("aftale").getJSONObject(i).get("timeStart").toString();
            String[] placeholder1=dato.split(" ");
            String[] placeholder=placeholder1[0].split("-");
            int month = Integer.parseInt(placeholder[1]);
            int day = Integer.parseInt(placeholder[2]);
            if (month<10){
                placeholder[1]=placeholder[1].substring(1);
            }
            if (day<10){
                placeholder[2]=placeholder[2].substring(1);
            }
            dato = placeholder[0]+"-"+placeholder[1]+"-"+placeholder[2];

            if (dato.startsWith(from)) {
                aftaleListe.addAftaler(new Gson().fromJson(grp1.getJSONObject("aftaleListe").getJSONArray("aftale").getJSONObject(i).toString(),Aftale.class));
            }
        }*/


        JSONObject grp2 = apiDAO.getApiDAOOBJ().getJsonOBJ("http://ekg2.diplomportal.dk:8080/data/aftaler",System.getenv("ApiKeyGrp2"));
        for (int i = 0; i < grp2.getJSONObject("aftaleListe").getJSONArray("aftale").length(); i++) {
            String dato = grp2.getJSONObject("aftaleListe").getJSONArray("aftale").getJSONObject(i).get("timeStart").toString();
            String[] placeholder1=dato.split(" ");
            String[] placeholder=placeholder1[0].split("-");
            int month = Integer.parseInt(placeholder[1]);
            int day = Integer.parseInt(placeholder[2]);
            if (month<10){
                placeholder[1]=placeholder[1].substring(1);
            }
            if (day<10){
                placeholder[2]=placeholder[2].substring(1);
            }
            dato = placeholder[0]+"-"+placeholder[1]+"-"+placeholder[2];

            if (dato.startsWith(from)) {
                aftaleListe.addAftaler(new Gson().fromJson(grp2.getJSONObject("aftaleListe").getJSONArray("aftale").getJSONObject(i).toString(),Aftale.class));
            }
        }
        /*
        JSONObject grp4 = apiDAO.getApiDAOOBJ().getJsonOBJ("http://ekg2.diplomportal.dk:8080/data/aftaler",System.getenv("ApiKeyGrp4"));
        for (int i = 0; i < grp4.getJSONObject("aftaleListe").getJSONArray("aftale").length(); i++) {
            String dato = grp4.getJSONObject("aftaleListe").getJSONArray("aftale").getJSONObject(i).get("timeStart").toString();
            String[] placeholder1=dato.split(" ");
            String[] placeholder=placeholder1[0].split("-");
            int month = Integer.parseInt(placeholder[1]);
            int day = Integer.parseInt(placeholder[2]);
            if (month<10){
                placeholder[1]=placeholder[1].substring(1);
            }
            if (day<10){
                placeholder[2]=placeholder[2].substring(1);
            }
            dato = placeholder[0]+"-"+placeholder[1]+"-"+placeholder[2];

            if (dato.startsWith(from)) {
                aftaleListe.addAftaler(new Gson().fromJson(grp4.getJSONObject("aftaleListe").getJSONArray("aftale").getJSONObject(i).toString(),Aftale.class));
            }
        }
        JSONObject grp5 = apiDAO.getApiDAOOBJ().getJsonOBJ("http://ekg2.diplomportal.dk:8080/data/aftaler",System.getenv("ApiKeyGrp5"));
        for (int i = 0; i < grp5.getJSONObject("aftaleListe").getJSONArray("aftale").length(); i++) {
            String dato = grp5.getJSONObject("aftaleListe").getJSONArray("aftale").getJSONObject(i).get("timeStart").toString();
            String[] placeholder1=dato.split(" ");
            String[] placeholder=placeholder1[0].split("-");
            int month = Integer.parseInt(placeholder[1]);
            int day = Integer.parseInt(placeholder[2]);
            if (month<10){
                placeholder[1]=placeholder[1].substring(1);
            }
            if (day<10){
                placeholder[2]=placeholder[2].substring(1);
            }
            dato = placeholder[0]+"-"+placeholder[1]+"-"+placeholder[2];

            if (dato.startsWith(from)) {
                aftaleListe.addAftaler(new Gson().fromJson(grp5.getJSONObject("aftaleListe").getJSONArray("aftale").getJSONObject(i).toString(),Aftale.class));
            }
        }*/

        return aftaleListe;
    }

    public AftaleListe getALLGroupsAftaleCPR(String CPR) throws SQLException, OurException {
        AftaleListe aftaleListe = cprSearch(CPR);

        /*
        JSONObject grp1 = apiDAO.getApiDAOOBJ().getJsonOBJ("http://localhost:8080/SemesterProjekt_3_war/data/aftaler?cpr=" + CPR,("Bearer "+System.getenv("ApiKeyGrp1"))));
        for (int i = 0; i < grp1.getJSONObject("aftaleListe").getJSONArray("aftale").length(); i++) {
            System.out.println(grp1.getJSONObject("aftaleListe").getJSONArray("aftale").getJSONObject(i));
            aftaleListe.addAftaler(new Gson().fromJson(grp1.getJSONObject("aftaleListe").getJSONArray("aftale").getJSONObject(i).toString(), Aftale.class));
        }*/



        JSONObject grp2 = apiDAO.getApiDAOOBJ().getJsonOBJ("http://ekg2.diplomportal.dk:8080/data/aftaler?cpr=" + CPR,System.getenv("ApiKeyGrp2"));
        for (int i = 0; i < grp2.getJSONObject("aftaleListe").getJSONArray("aftale").length(); i++) {

            aftaleListe.addAftaler(new Gson().fromJson(grp2.getJSONObject("aftaleListe").getJSONArray("aftale").getJSONObject(i).toString(), Aftale.class));
        }
        /*
        JSONObject grp4 = apiDAO.getApiDAOOBJ().getJsonOBJ("http://localhost:8080/SemesterProjekt_3_war/data/aftaler?cpr=" + CPR,("Bearer "+System.getenv("ApiKeyGrp4")));
        for (int i = 0; i < grp1.getJSONObject("aftaleListe").getJSONArray("aftale").length(); i++) {

            aftaleListe.addAftaler(new Gson().fromJson(grp1.getJSONObject("aftaleListe").getJSONArray("aftale").getJSONObject(i).toString(), Aftale.class));
        }

        JSONObject grp5 = apiDAO.getApiDAOOBJ().getJsonOBJ("http://localhost:8080/SemesterProjekt_3_war/data/aftaler?cpr=" + CPR,("Bearer "+System.getenv("ApiKeyGrp5")));
        for (int i = 0; i < grp1.getJSONObject("aftaleListe").getJSONArray("aftale").length(); i++) {

            aftaleListe.addAftaler(new Gson().fromJson(grp1.getJSONObject("aftaleListe").getJSONArray("aftale").getJSONObject(i).toString(), Aftale.class));
        }*/

        return aftaleListe;
    }

}
