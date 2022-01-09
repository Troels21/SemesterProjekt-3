package dataAccesLayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginSQL {

    private LoginSQL() {
    }

    static private final LoginSQL LOGIN_SQL_OBJ = new LoginSQL();

    static public LoginSQL getLoginSqlObj() {
        return LOGIN_SQL_OBJ;
    }

    public String returnLoginUserDB(String username) throws SQLException {
        SQL.getSqlOBJ().makeConnectionSQL();
        PreparedStatement preparedStatement = SQL.getSqlOBJ().myConn.prepareStatement("SELECT * FROM listeDB.LoginOplysninger WHERE USERNAME = ?;");
        preparedStatement.setString(1, username);
        String svar = "";
        try {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                svar = svar + rs.getString(1);
                svar = svar + "|" + rs.getString(2);
                svar = svar + "|" + rs.getString(3);
                svar = svar + "|" + rs.getString(4);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        SQL.getSqlOBJ().removeConnectionSQL();
        return svar;
    }
}
