package api;

import controller.JWTHandler;
import model.User;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class AuthFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        /* Kontrol af private key på aftaler endpoint */
        //Hvis det ikke er login siden udføre vi kontrol af token
        if (!"login".equals(containerRequestContext.getUriInfo().getPath())) {
            if (containerRequestContext.getHeaderString("Authorization") == null) {
                throw new WebApplicationException("Ingen Token", 401);
            }

            if ("aftaler".equals(containerRequestContext.getUriInfo().getPath()) ||
                    "ekgSessions".equals(containerRequestContext.getUriInfo().getPath()) ||
                    "ekgSessions/measurements".equals(containerRequestContext.getUriInfo().getPath())) {
                if (!containerRequestContext.getHeaderString("Authorization").equals("Bearer "+System.getenv("ApiKeyGrp3"))) {
                    throw new WebApplicationException("Forkert Login", 401);
                }
            }
            else {
                try {
                    User user = JWTHandler.validateToken(containerRequestContext.getHeaderString("Authorization"));
                    containerRequestContext.setProperty("user",user);
                } catch (Exception e) {
                    throw new WebApplicationException("Invalid Token", 401);
                }

            }
        }

    }
}
