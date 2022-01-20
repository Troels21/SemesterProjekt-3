package dataAccesLayer;

import exceptions.OurException;
import model.Aftale;
import model.AftaleListe;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AftaleSQL {

    private AftaleSQL() {
    }

    static private final AftaleSQL aftaleSQL_OBJ = new AftaleSQL();

    static public AftaleSQL getAftaleSQLObj() {
        return aftaleSQL_OBJ;
    }

    //gets sqldata, makes Aftale, adds to aftaleListe
    public AftaleListe getAftaleListeDateTime(String fra, String til) throws SQLException {
        SQL.getSqlOBJ().makeConnectionSQL();
        AftaleListe aftaleListe = new AftaleListe();
        try {
            PreparedStatement pp = SQL.getSqlOBJ().myConn.prepareStatement("SELECT * FROM aftaler \n" +
                    "INNER JOIN patient\n" +
                    "ON aftaler.patientID=patient.patientID WHERE TimeStart BETWEEN ? and ?;");
            pp.setString(1, fra);
            pp.setString(2, til);

            ResultSet rs = pp.executeQuery();

            while (rs.next()) {
                Aftale aftale = new Aftale();
                aftale.setCPR(rs.getString(8));
                aftale.setTimeStart(rs.getString(2));
                aftale.setTimeEnd(rs.getString(3));
                aftale.setNotat(rs.getString(4));
                aftale.setKlinikID(rs.getString(5));
                aftale.setID(rs.getString(6));

                aftaleListe.addAftaler(aftale);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SQL.getSqlOBJ().removeConnectionSQL();

        return aftaleListe;
    }

    public void insertAftaleSQL(Aftale aftale) throws OurException {

        try {
            PatientSQL.getPatientSQLobj().createNewPatientIfNotExist(aftale.getCPR());

            int id = PatientSQL.getPatientSQLobj().getPatientID(aftale.getCPR());

            SQL.getSqlOBJ().makeConnectionSQL();
            PreparedStatement pp = SQL.getSqlOBJ().myConn.prepareStatement("INSERT INTO listeDB.aftaler (patientID, TimeStart, TimeEnd, Notat, KlinikId) values(?,?,?,?,?);");

            pp.setInt(1, id);  //patientID
            pp.setString(2, aftale.getTimeStart());  //starttime
            pp.setString(3, aftale.getTimeEnd());  //endtime
            pp.setString(4, aftale.getNotat());  //note
            pp.setString(5, aftale.getKlinikID()); //klinikif

            pp.execute();

            SQL.getSqlOBJ().removeConnectionSQL();
        } catch (SQLException throwables) {
            OurException ex = new OurException();
            ex.setMessage("Tiden er allerede optaget.");
            throw ex;
        }
    }
    //gets sqldata, makes Aftale, adds to aftaleListe
    public AftaleListe getAftalerListe() throws SQLException {
        SQL.getSqlOBJ().makeConnectionSQL();
        AftaleListe aftaleListe = new AftaleListe();
        String query = "SELECT * FROM aftaler\n" +
                "INNER JOIN patient\n" +
                "ON aftaler.patientID=patient.patientID";
        try {
            ResultSet rs = SQL.getSqlOBJ().myStatement.executeQuery(query);

            while (rs.next()) {
                Aftale aftale = new Aftale();
                aftale.setCPR(rs.getString(8));
                aftale.setTimeStart(rs.getString(2));
                aftale.setTimeEnd(rs.getString(3));
                aftale.setNotat(rs.getString(4));
                aftale.setID(rs.getString(6));
                aftale.setKlinikID(rs.getString(5));

                aftaleListe.addAftaler(aftale);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        SQL.getSqlOBJ().removeConnectionSQL();

        return aftaleListe;
    }

    //Finds aftale SQL data from cpr
    public AftaleListe cprSearch(String cpr) throws SQLException {
        SQL.getSqlOBJ().makeConnectionSQL();
        PreparedStatement pp = SQL.getSqlOBJ().myConn.prepareStatement("SELECT * FROM aftaler\n" +
                "INNER JOIN patient\n" +
                "ON aftaler.patientID=patient.patientID where patient.cpr= ? ");
        AftaleListe aftaleListe = new AftaleListe();
        try {
            pp.setString(1, cpr);
            ResultSet rs = pp.executeQuery();

            while (rs.next()) {
                Aftale aftale = new Aftale();
                aftale.setCPR(rs.getString(8));
                aftale.setTimeStart(rs.getString(2));
                aftale.setTimeEnd(rs.getString(3));
                aftale.setNotat(rs.getString(4));
                aftale.setID(rs.getString(6));
                aftale.setKlinikID(rs.getString(5));

                aftaleListe.addAftaler(aftale);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SQL.getSqlOBJ().removeConnectionSQL();
        return aftaleListe;
    }

    public void deleteAftaleIn(String deleteIn) throws SQLException {
        SQL.getSqlOBJ().makeConnectionSQL();
        PreparedStatement pp = SQL.getSqlOBJ().myConn.prepareStatement("DELETE FROM aftaler where aftaleID in "+deleteIn+" ;");
        try {
            pp.execute();
            SQL.getSqlOBJ().removeConnectionSQL();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
