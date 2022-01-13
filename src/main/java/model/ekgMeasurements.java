package model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;


@XmlRootElement(name="measurements")
@XmlAccessorType(XmlAccessType.FIELD)
public class ekgMeasurements {
    @Override
    public String toString() {
        return "ekgMeasurements{" +
                "measurment=" + measurment +
                '}';
    }

    private List<Double> measurment = new ArrayList<>();

    public List<Double> getMeasurments() {
        return measurment;
    }

    public void setMeasurment(List<Double> measurment) {
        this.measurment = measurment;
    }

    public void addMeasurments(Double mearsurement) {
        this.measurment.add(mearsurement);
    }
}
