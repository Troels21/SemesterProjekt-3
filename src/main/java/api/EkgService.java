package api;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;


@Path("ekg")
public class EkgService {

    @GET
    public String hello(){
        return "hello";
    }

    @POST
    public String test(String string,@Context HttpHeaders httpHeaders){
        System.out.println(string);
        System.out.println(httpHeaders.getRequestHeader("Identifier").get(0));


        return "jeg fik : "+string;
    }

}
