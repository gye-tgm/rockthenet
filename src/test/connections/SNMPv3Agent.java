package test.connections;

import java.io.File;
import java.io.IOException;

import org.snmp4j.TransportMapping;
import org.snmp4j.agent.BaseAgent;
import org.snmp4j.agent.CommandProcessor;
import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.MOGroup;
import org.snmp4j.agent.ManagedObject;
import org.snmp4j.agent.mo.snmp.SnmpCommunityMIB;
import org.snmp4j.agent.mo.snmp.SnmpNotificationMIB;
import org.snmp4j.agent.mo.snmp.SnmpTargetMIB;
import org.snmp4j.agent.mo.snmp.StorageType;
import org.snmp4j.agent.mo.snmp.VacmMIB;
import org.snmp4j.agent.security.MutableVACM;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.AuthSHA;
import org.snmp4j.security.PrivDES;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModel;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.transport.TransportMappings;

public class SNMPv3Agent extends BaseAgent {
	private String address;
	private int port;
	
	public SNMPv3Agent(String address, int port) throws IOException {
		super(new File("conf.agent"), new File("bootCounter.agent"), new CommandProcessor(new OctetString(MPv3.createLocalEngineID())));
	        
	    this.address = address;
	    this.port = port;
	}
	
	@Override
	protected void addViews(VacmMIB vacm) {
	    vacm.addGroup(SecurityModel.SECURITY_MODEL_USM, new OctetString("SHADES"), new OctetString("v3group"), StorageType.nonVolatile);
	    vacm.addAccess(new OctetString("v3group"), new OctetString(), SecurityModel.SECURITY_MODEL_USM, SecurityLevel.AUTH_PRIV, MutableVACM.VACM_MATCH_EXACT, new OctetString("fullReadView"), new OctetString("fullWriteView"), new OctetString("fullNotifyView"), StorageType.nonVolatile);
	    vacm.addViewTreeFamily(new OctetString("fullReadView"), new OID("1.3"), new OctetString(), VacmMIB.vacmViewIncluded, StorageType.nonVolatile);
	}

	protected void addUsmUser(USM usm) {
	    UsmUser user = new UsmUser(new OctetString("SHADES"), AuthSHA.ID, new OctetString("SHADESAuthPassword"), PrivDES.ID, new OctetString("SHADESPrivPassword"));
	    usm.addUser(user.getSecurityName(), usm.getLocalEngineID(), user);
	    
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
        getServer().addContext(new OctetString(("public")));
        finishInit();
        run();
        sendColdStartNotification();
    }
    
    public void stop() {
    	try {
    		super.stop();
    	} catch (Exception e) {} // prevent Exception on double-close; required for a test-case
    }

    protected void initTransportMappings() throws IOException {
        transportMappings =  new TransportMapping[1];
        transportMappings[0] = TransportMappings.getInstance().createTransportMapping(GenericAddress.parse("udp:" + address + "/" + port));
    }
    
    @Override
	protected void unregisterManagedObjects() { }
	@Override
	protected void addCommunities(SnmpCommunityMIB arg0) { }
	@Override
	protected void addNotificationTargets(SnmpTargetMIB arg0, SnmpNotificationMIB arg1) { }
	@Override
	protected void registerManagedObjects() { }
}
