package test.connections.snmp;

import java.io.File;
import java.io.IOException;

import org.snmp4j.TransportMapping;
import org.snmp4j.agent.BaseAgent;
import org.snmp4j.agent.CommandProcessor;
import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.MOGroup;
import org.snmp4j.agent.ManagedObject;
import org.snmp4j.agent.mo.snmp.RowStatus;
import org.snmp4j.agent.mo.snmp.SnmpCommunityMIB;
import org.snmp4j.agent.mo.snmp.SnmpNotificationMIB;
import org.snmp4j.agent.mo.snmp.SnmpTargetMIB;
import org.snmp4j.agent.mo.snmp.StorageType;
import org.snmp4j.agent.mo.snmp.VacmMIB;
import org.snmp4j.agent.security.MutableVACM;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModel;
import org.snmp4j.security.USM;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;
import org.snmp4j.transport.TransportMappings;

/* TODO: add support for `securityName` */
public class SNMPv2Agent extends BaseAgent {
	private String address;
	private int port;
	private String community;
	 
	
    public SNMPv2Agent(String address, int port, String community) throws IOException {
        super(new File("conf.agent"), new File("bootCounter.agent"), new CommandProcessor(new OctetString(MPv3.createLocalEngineID())));
        
        this.address = address;
        this.port = port;
        this.community = community;
    }

	@Override
	protected void addCommunities(SnmpCommunityMIB communityMiB) {
		Variable[] com2sec = new Variable[] {
                new OctetString(community), 			// community name
                new OctetString(community), 			// security name
                getAgent().getContextEngineID(), 		// local engine ID
                new OctetString(community), 			// default context name
                new OctetString(), 						// transport tag
                new Integer32(StorageType.nonVolatile), // storage type
                new Integer32(RowStatus.active) 		// row status
        };
		
        communityMiB.getSnmpCommunityEntry().addRow(communityMiB.getSnmpCommunityEntry().createRow(new OctetString(community + "2" + community).toSubIndex(true), com2sec));
	}

	@Override
	protected void addViews(VacmMIB vacmMiB) {
		vacmMiB.addGroup(SecurityModel.SECURITY_MODEL_SNMPv2c, new OctetString(community), new OctetString(community), StorageType.nonVolatile);
        vacmMiB.addAccess(new OctetString(community), new OctetString(community), SecurityModel.SECURITY_MODEL_ANY, SecurityLevel.NOAUTH_NOPRIV, MutableVACM.VACM_MATCH_EXACT, new OctetString("fullReadView"), new OctetString("fullWriteView"), new OctetString("fullNotifyView"), StorageType.nonVolatile);
        vacmMiB.addViewTreeFamily(new OctetString("fullReadView"), new OID("1.3"), new OctetString(), VacmMIB.vacmViewIncluded, StorageType.nonVolatile);
	}
	
	public void registerManagedObject(ManagedObject mo) {
		try {
			server.register(mo, null);
	    } catch (DuplicateRegistrationException ex) {
	    	throw new RuntimeException(ex);
	    }
	}
	 
	public void unregisterManagedObject(MOGroup moGroup) {
		moGroup.unregisterMOs(server, getContext(moGroup));
	}
 
    public void start() throws IOException {
        init();
        addShutdownHook();
        getServer().addContext(new OctetString(community));
        finishInit();
        run();
        sendColdStartNotification();
    }
    
    public void stop() {
    	try {
    		super.stop();
            Thread.sleep(100);
    	} catch (Exception e) {} // prevent Exception on double-close; required for a test-case
    }
    
    protected void initTransportMappings() throws IOException {
        transportMappings =  new TransportMapping[1];
        transportMappings[0] = TransportMappings.getInstance().createTransportMapping(GenericAddress.parse("udp:" + address + "/" + port));
    }

	@Override
	protected void registerManagedObjects() { }
	@Override
	protected void unregisterManagedObjects() { }
	@Override
	protected void addNotificationTargets(SnmpTargetMIB arg0, SnmpNotificationMIB arg1) { }
	@Override
	protected void addUsmUser(USM arg0) { }
}
