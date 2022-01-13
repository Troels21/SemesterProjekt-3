package api;

import com.google.gson.Gson;
import controller.AftaleController;
import controller.ekgController;
import dataAccesLayer.EkgSql;
import dataAccesLayer.PatientSQL;
import model.User;
import model.ekgMeasurements;
import model.ekgSessionList;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;


@Path("ekgSessions")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class EkgService {
    @Context
    ContainerRequestContext context;

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
    public String getSessionJson(@QueryParam("cpr") String cpr) throws SQLException {
        User user = (User) context.getProperty("user");
        if (user.isDoctor()) {
            if (cpr != null) {
                try {
                    return new Gson().toJson(ekgController.getEkgController().getAllSessions(cpr));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            return null;
        }else if (!user.isDoctor()) {
            return new Gson().toJson(ekgController.getEkgController().getAllSessions(user.getUsername()));
        }
        throw new WebApplicationException("You dont have rights", 401);
        }

    @Path("ekgSessionJson/{sessionID}/comment")
    @PUT
    public String updateEkgSession(@PathParam("sessionID") String sessionID, String comment) {
        User user = (User) context.getProperty("user");
        if (user.isDoctor()) {
            try {
                EkgSql.getEkgSql().updateEkgSession(sessionID, comment);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return "I got:" + sessionID + " and " + comment;
        }
        throw new WebApplicationException("You dont have rights", 401);
    }

    @Path("ekgMeasurementsJson")
    @GET
    public String getMeasurementJson(@QueryParam("sessionID") Integer sessionID, @QueryParam("klinikID") Integer klinikID) throws SQLException {
        User user = (User) context.getProperty("user");
        if (user.isDoctor()) {
        if (sessionID != null) {
            try {
                return new Gson().toJson(ekgController.getEkgController().getAllEKGMeasurements(sessionID,klinikID));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return null;
    }else if (!user.isDoctor()) {

            return new Gson().toJson(EkgSql.getEkgSql().getMeasurements(sessionID));
        }
        throw new WebApplicationException("You dont have rights", 401);
    }
}

