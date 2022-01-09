package api;

import dataAccesLayer.EkgSql;
import model.ekgMeasurements;
import model.ekgSessionList;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;


@Path("ekgSessions")
@Produces({MediaType.APPLICATION_XML})
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
    public String pythonDataReceive(String string, @Context HttpHeaders httpHeaders) {
        System.out.println(string);
        System.out.println(httpHeaders.getRequestHeader("Identifier").get(0));


        return "jeg fik : " + string;
    }

}
