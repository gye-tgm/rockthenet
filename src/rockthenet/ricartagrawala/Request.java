package rockthenet.ricartagrawala;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Gary Ye
 */
public class Request implements Serializable {
    public static final Integer FIREWALL = 1;

    private Date timestamp;
    private Integer ressourceId;

    public Request() {
        this(new Date(System.currentTimeMillis()), FIREWALL);
    }

    public Request(Date timestamp, Integer ressourceId) {
        this.timestamp = timestamp;
        this.ressourceId = ressourceId;
    }

    public Integer getRessourceId() {
        return ressourceId;
    }

    public void setRessourceId(Integer ressourceId) {
        this.ressourceId = ressourceId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
