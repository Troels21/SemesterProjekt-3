package controller;

import dataAccesLayer.LoginSQL;
import model.LoginData;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

import javax.ws.rs.WebApplicationException;
import java.sql.SQLException;

public class LoginController {

    private LoginController() {
    }

    static private final LoginController loginControllerOBJ = new LoginController();

    static public LoginController getLoginControllerOBJ() {
        return loginControllerOBJ;
    }

    public String doLogin(LoginData loginData) {
        try {
            // sql kald der kontrollere om brugeren eksitere
            String brugerListe = LoginSQL.getLoginSqlObj().returnLoginUserDB(loginData.getUsername());
            String[] opdelt = brugerListe.split("\\|");
            // kontrol af login og generer token
            if (hashControl(loginData.getPassword(), opdelt[1])) {
                User user = new User(loginData);
                if (opdelt[3].equals("1")) {
                    user.setDoctor(true);
                } else {
                    user.setDoctor(false);
                }
                return JWTHandler.generateJwtToken(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new WebApplicationException("fejl", 401);
    }

    /*public static void main(String[] args) {
        System.out.println(generateHash("4321"));
    }*/

    public static String generateHash(String pass) {
        String salt = (BCrypt.gensalt(10));
        System.out.println(salt);
        return BCrypt.hashpw(pass, salt);

    }

    public static boolean hashControl(String password, String hashedpassword) {
        return BCrypt.checkpw(password, hashedpassword);
    }

}

