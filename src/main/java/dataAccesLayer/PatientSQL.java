package dataAccesLayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientSQL {
    private PatientSQL() {
    }

    static private final PatientSQL patientSQL_OBJ = new PatientSQL();

    static public PatientSQL getPatientSQLobj() {
        return patientSQL_OBJ;
    }

    public Integer getPatientID(String cpr) throws SQLException {
        SQL.getSqlOBJ().makeConnectionSQL();
        PreparedStatement pp = SQL.getSqlOBJ().myConn.prepareStatement("SELECT patientID \n" +
                "FROM patient \n" +
                "WHERE patient.cpr = ? ");

        pp.setString(1, cpr);
        try {
            ResultSet rs = pp.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                SQL.getSqlOBJ().removeConnectionSQL();
                return id;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        SQL.getSqlOBJ().removeConnectionSQL();
        return null;
    }

    public String getPatientCPR(int patientID) throws SQLException {
        SQL.getSqlOBJ().makeConnectionSQL();
        PreparedStatement pp = SQL.getSqlOBJ().myConn.prepareStatement("SELECT cpr\n" +
                "FROM patient\n" +
                "WHERE patient.patientID = ? ");

        pp.setInt(1, patientID);
        try {
            ResultSet rs = pp.executeQuery();
            while (rs.next()) {
                String cpr = rs.getString(1);
                SQL.getSqlOBJ().removeConnectionSQL();
                return cpr;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        SQL.getSqlOBJ().removeConnectionSQL();
        return null;
    }

    public void createNewPatientIfNotExist(String cpr) throws SQLException {
        if (getPatientID(cpr) == null) {
            SQL.getSqlOBJ().makeConnectionSQL();
            PreparedStatement pp = SQL.getSqlOBJ().myConn.prepareStatement("insert into patient (cpr) values ( ? );");
            pp.setString(1, cpr);
            pp.execute();
            SQL.getSqlOBJ().removeConnectionSQL();
        }
    }
}
