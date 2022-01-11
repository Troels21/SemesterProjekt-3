package api;

import com.google.gson.Gson;
import controller.ekgController;
import dataAccesLayer.EkgSql;
import model.ekgMeasurements;
import model.ekgSessionList;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;


@Path("ekgSessions")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class EkgService {

    @GET
    public ekgSessionList getEkgSession(@QueryParam("cpr") String cpr) throws SQLException {
        if (cpr != null) {
            return EkgSql.getEkgSql().getEkgSessions(cpr);
        }
        return null;
    }

    @Path("measurements")
    @GET
    public ekgMeasurements getEkgdata(@QueryParam("sessionID") Integer sesID) throws SQLException {
        if (sesID != null) {
            return EkgSql.getEkgSql().getMeasurements(sesID);
        }
        return null;
    }

    @Path("measurements")
    @POST
    public String pythonDataReceive(String data, @Context HttpHeaders cprHeader) {
        ekgController.getEkgController().validate(data, cprHeader.getRequestHeader("Identifier").get(0));

        return "jeg fik : " + data;
    }

    @Path("ekgSessionJson")
    @GET
    public String getSessionJson(@QueryParam("cpr") String cpr) {
        if (cpr != null) {
            try {
                return new Gson().toJson(EkgSql.getEkgSql().getEkgSessions(cpr));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return null;
    }

    @Path("ekgSessionJson/{sessionID}/comment")
    @PUT
    public String updateEkgSession(@PathParam("sessionID") String sessionID, String comment){
        try {
            EkgSql.getEkgSql().updateEkgSession(sessionID,comment);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "I got:" + sessionID +" and "+comment;
    }

    @Path("ekgMeasurementsJson")
    @GET
    public String getMeasurementJson(@QueryParam("sessionID") Integer sessionID) {
        if (sessionID != null) {
            try {
                return new Gson().toJson(EkgSql.getEkgSql().getMeasurements(sessionID));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return null;
    }

}

