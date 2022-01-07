package dataAccesLayer;

import controller.ekgController;
import model.ekgMeasurements;

import java.util.List;

public class EkgSql {

    private EkgSql() {
    }

    static private final EkgSql EKG_SQL = new EkgSql();

    static public EkgSql getEkgSql() {
        return EKG_SQL;
    }

    private void sqlInsertEkgSession(){

    }

    private void sqlInsertEkgMeasurements(){

    }

    private model.ekgMeasurements getMeasurements(List<Double> liste){

        ekgMeasurements model = new ekgMeasurements();
        return model;
    }


}
