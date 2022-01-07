package model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


@XmlRootElement(name="measurements")
@XmlAccessorType(XmlAccessType.FIELD)
public class ekgMeasurements {
    @Override
    public String toString() {
        return "ekgMeasurements{" +
                "measurments=" + measurments +
                '}';
    }

    private List<Double> measurments;

    public List<Double> getMeasurments() {
        return measurments;
    }

    public void addMeasurments(Double mearsurement) {
        this.measurments.add(mearsurement);
    }
}
