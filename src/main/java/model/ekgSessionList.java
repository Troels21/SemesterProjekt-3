package model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "sessions")
@XmlSeeAlso(ekgSession.class)
@XmlAccessorType(XmlAccessType.FIELD)

public class ekgSessionList {
    public List<ekgSession> getEkgSessionList() {
        return ekgSessionList;
    }

    public void setEkgSessionList(List<ekgSession> ekgSessionList) {
        this.ekgSessionList = ekgSessionList;
    }

    @XmlElement(name = "ekgSession")
    List<ekgSession> ekgSessionList = new ArrayList<>();

    public void addEkgSession(ekgSession ekgses) {
        ekgSessionList.add(ekgses);
    }
}

