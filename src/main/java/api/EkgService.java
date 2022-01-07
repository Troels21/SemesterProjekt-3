package api;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;


@Path("ekgSessions")
public class EkgService {

    @GET
    public String getEkgSession(){
        return "hello";
    }

    @Path("measurements")
    @GET
    public String getEkgdata(){
        return "hello";
    }
    @Path("measurements")
    @POST
    public String pythonDataReceive(String string, @Context HttpHeaders httpHeaders){
        System.out.println(string);
        System.out.println(httpHeaders.getRequestHeader("Identifier").get(0));


        return "jeg fik : "+string;
    }

}
