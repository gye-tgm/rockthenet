package rockthenet.firewall;

/**
 * @author Gary
 */
public class Policy {
    private Integer id;
    private String srcZone;
    private String dstZone;
    private String srcAddress;
    private String dstAddress;
    private Integer service;
    private Integer action;
    private Integer activeStatus;
    private String name;

    public Policy(Integer id, String srcZone, String dstZone, String srcAddress, String dstAddress, Integer service, Integer action, Integer activeStatus, String name) {
        this.id = id;
        this.srcZone = srcZone;
        this.dstZone = dstZone;
        this.srcAddress = srcAddress;
        this.dstAddress = dstAddress;
        this.service = service;
        this.action = action;
        this.activeStatus = activeStatus;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getSrcZone() {
        return srcZone;
    }

    public String getDstZone() {
        return dstZone;
    }

    public String getSrcAddress() {
        return srcAddress;
    }

    public String getDstAddress() {
        return dstAddress;
    }

    public Integer getService() {
        return service;
    }

    public Integer getAction() {
        return action;
    }

    public Integer getActiveStatus() {
        return activeStatus;
    }

    public String getName() {
        return name;
    }
}
