package dataAccesLayer;

import model.ekgMeasurements;
import model.ekgSession;
import model.ekgSessionList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class EkgSql {

    private EkgSql() {
    }

    static private final EkgSql EKG_SQL = new EkgSql();

    static public EkgSql getEkgSql() {
        return EKG_SQL;
    }

    public void sqlInsertEkgSession(String markers, String comments, String cpr) throws SQLException {
        PatientSQL.getPatientSQLobj().createNewPatientIfNotExist(cpr);

        int id = PatientSQL.getPatientSQLobj().getPatientID(cpr);
        SQL.getSqlOBJ().makeConnectionSQL();
        try {
            PreparedStatement pp = SQL.getSqlOBJ().myConn.prepareStatement("insert into malingstidspunkt(markers, comments, patientID)values(?,?,?);");

            pp.setString(1, markers);
            pp.setString(2, comments);
            pp.setInt(3, id);

            pp.execute();
            SQL.getSqlOBJ().removeConnectionSQL();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public ekgSessionList getEkgSessions(String cpr) throws SQLException {
        SQL.getSqlOBJ().makeConnectionSQL();
        PreparedStatement pp = SQL.getSqlOBJ().myConn.prepareStatement("SELECT * FROM malingstidspunkt\n" +
                "INNER JOIN patient\n" +
                "ON malingstidspunkt.patientID=patient.patientID where patient.cpr= ? ");
        try {
            pp.setString(1, cpr);
            ResultSet rs = pp.executeQuery();
            ekgSessionList ekgsessionList = new ekgSessionList();
            while (rs.next()) {
                ekgSession ekgses = new ekgSession();
                String[] markerArray = rs.getString(3).split(",");

                ekgses.setTimeStart(rs.getString(1).substring(0, 16));
                ekgses.setComment(rs.getString(4));
                ekgses.setCpr(rs.getString(7));
                ekgses.setSessionID(rs.getInt(2));
                ekgses.setMarkers(Arrays.asList(markerArray));
                ekgsessionList.addEkgSession(ekgses);
            }
            return ekgsessionList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }


    public void sqlInsertEkgMeasurements(String cpr, double[] measurement) throws SQLException {
        int patID = PatientSQL.getPatientSQLobj().getPatientID(cpr);
        int sesID = getSessionID(patID);
        try {
            String sql = "INSERT INTO ekg(maling,sessionID,patientID) values(" + measurement[0] + "," + sesID + "," + patID + ")";

            for (int i = 1; i < measurement.length; i++) {
                sql = sql + ",(" + measurement[i] + "," + sesID + "," + patID + ")";
            }

            sql = sql + ";";


            SQL.getSqlOBJ().makeConnectionSQL();
            PreparedStatement pp = SQL.getSqlOBJ().myConn.prepareStatement(sql);
            pp.execute();
            SQL.getSqlOBJ().removeConnectionSQL();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ekgMeasurements getMeasurements(Integer sessionID) throws SQLException {
        SQL.getSqlOBJ().makeConnectionSQL();
        PreparedStatement pp = SQL.getSqlOBJ().myConn.prepareStatement("SELECT * FROM ekg where sessionID = ? ;");
        ekgMeasurements measurements = new ekgMeasurements();
        try {
            pp.setInt(1, sessionID);
            ResultSet rs = pp.executeQuery();

            while (rs.next()) {
                measurements.addMeasurments(rs.getDouble(1));
            }
            return measurements;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer getSessionID(int patientID) throws SQLException {
        int sesID = 0;
        SQL.getSqlOBJ().makeConnectionSQL();
        PreparedStatement pp = SQL.getSqlOBJ().myConn.prepareStatement("SELECT sessionID \n" +
                "FROM malingstidspunkt \n" +
                "WHERE malingstidspunkt.patientID = ? ");

        pp.setInt(1, patientID);

        try {
            ResultSet rs = pp.executeQuery();
            while (rs.next()) {
                sesID = rs.getInt(1);
            }
            SQL.getSqlOBJ().removeConnectionSQL();
            if (sesID != 0) {
                return sesID;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        SQL.getSqlOBJ().removeConnectionSQL();
        return null;
    }

    public void updateEkgSession(String sesID, String comments) throws SQLException {
        SQL.getSqlOBJ().makeConnectionSQL();
        PreparedStatement pp = SQL.getSqlOBJ().myConn.prepareStatement("UPDATE malingstidspunkt\n" +
                "SET comments = ?\n" +
                "WHERE sessionID = ? ;");

        try {
            pp.setString(1, comments);
            pp.setString(2, sesID);

            pp.execute();
            SQL.getSqlOBJ().removeConnectionSQL();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
