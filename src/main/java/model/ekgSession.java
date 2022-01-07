package model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="sessions")
@XmlAccessorType(XmlAccessType.FIELD)
public class ekgSession {

    private int sessionID;
    private String cpr;
    private String timeStart;
    private List<Integer> markers;
    private String comment;

    @Override
    public String toString() {
        return "ekgSession{" +
                "sessionID=" + sessionID +
                ", cpr='" + cpr + '\'' +
                ", timeStart='" + timeStart + '\'' +
                ", markers=" + markers.toString() +
                ", comment='" + comment + '\'' +
                '}';
    }

    public int getSessionID() {
        return sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    public String getCpr() {
        return cpr;
    }

    public void setCpr(String cpr) {
        this.cpr = cpr;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public List<Integer> getMarkers() {
        return markers;
    }

    public void addMarkers(int marker) {
        this.markers.add(marker);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


}
