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
            // SQL does user exist
            String brugerListe = LoginSQL.getLoginSqlObj().returnLoginUserDB(loginData.getUsername());
            String[] opdelt = brugerListe.split("\\|");
            // Control password
            if (hashControl(loginData.getPassword(), opdelt[1])) {
                User user = new User(loginData);
                if (opdelt[3].equals("1")) {
                    user.setDoctor(true);
                } else {
                    user.setDoctor(false);
                }
                //Give JavaWebToken
                return JWTHandler.generateJwtToken(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new WebApplicationException("fejl", 401);
    }

    //public static void main(String[] args) {
      //  System.out.println(generateHash("password"));
    //}

    public static String generateHash(String pass) {
        String salt = (BCrypt.gensalt(10));
        System.out.println(salt);
        return BCrypt.hashpw(pass, salt);

    }

    public static boolean hashControl(String password, String hashedpassword) {
        return BCrypt.checkpw(password, hashedpassword);
    }

}

