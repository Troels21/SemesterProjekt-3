package api;

import com.google.gson.Gson;
import controller.AftaleController;
import dataAccesLayer.AftaleSQL;
import exceptions.OurException;
import model.AftaleListe;
import model.User;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;

@Path("aftaler")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_XML})
public class AftaleService {

    @Context
    ContainerRequestContext context;

    @GET
    public AftaleListe getPatient(@QueryParam("cpr") String cpr) throws SQLException, OurException {
        return AftaleController.getAftaleControllerOBJ().cprSearch(cpr);
    }

    @Path("aftalerSQL")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String makepatientSQL(@QueryParam("cpr") String cpr, @QueryParam("timestart")
            String timestart, @QueryParam("timeend") String timeend, @QueryParam("note") String notat) throws SQLException, OurException {
        User user = (User) context.getProperty("user");
        if (user.isDoctor()) {//Check for user access
            return AftaleController.getAftaleControllerOBJ().createAftale(cpr, timestart, timeend, notat);
        } else if (!user.isDoctor()) {//Check for user access
            return AftaleController.getAftaleControllerOBJ().createAftale(user.getUsername(), timestart, timeend, notat);
        }
        throw new WebApplicationException("You dont have rights", 401);
    }

    @Path("aftalerSQL")
    @GET
    public String selectFromTime(@QueryParam("from") String from, @QueryParam("to") String to) throws SQLException, OurException {
        User user = (User) context.getProperty("user");
        if (user.isDoctor()) {//Check for user access
            return new Gson().toJson(AftaleController.getAftaleControllerOBJ().getAllGroupsAftaleFromTo(from,to));
        } else if (!user.isDoctor()) {//Check for user access
            return new Gson().toJson(AftaleController.getAftaleControllerOBJ().getALLGroupsAftaleCPR(user.getUsername()));
        }
        throw new WebApplicationException("You dont have rights", 401);
    }

    @Path("aftalerSQL")
    @DELETE
    public String updateEkgSession(@QueryParam("numberToDelete") String data) throws SQLException {
        User user = (User) context.getProperty("user");
        if (user.isDoctor()) {//Check for user access
            AftaleSQL.getAftaleSQLObj().deleteAftaleIn(data);
            return "I got:" + data;
        }
        throw new WebApplicationException("You dont have rights", 401);
    }
}
