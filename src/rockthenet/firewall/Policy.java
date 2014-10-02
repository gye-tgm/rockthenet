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

    public Policy(){

    }
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
    
    @Override
    public String toString() {
    	StringBuilder s = new StringBuilder();
    	
    	s.append("id: \t\t" + id);
    	s.append("\nname: \t\t" + name);
    	s.append("\nsrcZone: \t" + srcZone);
    	s.append("\ndstZone: \t" + dstZone);
    	s.append("\nsrcAddress: \t" + srcAddress);
    	s.append("\ndstAddress: \t" + dstAddress);
    	s.append("\nservice: \t" + service);
    	s.append("\naction: \t" + action);
    	s.append("\nactiveStatus: \t" + activeStatus);
    	
    	return s.toString();
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

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSrcZone(String srcZone) {
        this.srcZone = srcZone;
    }

    public void setDstZone(String dstZone) {
        this.dstZone = dstZone;
    }

    public void setSrcAddress(String srcAddress) {
        this.srcAddress = srcAddress;
    }

    public void setDstAddress(String dstAddress) {
        this.dstAddress = dstAddress;
    }

    public void setService(Integer service) {
        this.service = service;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public void setActiveStatus(Integer activeStatus) {
        this.activeStatus = activeStatus;
    }

    public void setName(String name) {
        this.name = name;
    }
}
