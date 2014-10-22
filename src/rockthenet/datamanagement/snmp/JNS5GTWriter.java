package rockthenet.datamanagement.snmp;

import org.snmp4j.log.Log4jLogFactory;
import rockthenet.connections.ConnectionException;
import rockthenet.connections.ssh.SSHConnection;
import rockthenet.datamanagement.IDataWriter;
import rockthenet.dictionaries.JNS5GTValuesToEnglishDictionary;
import rockthenet.firewall.Policy;
import rockthenet.firewall.jns5gt.JNS5GTPolicy;

/**
 * Created by gary on 28/09/14.
 */
public class JNS5GTWriter implements IDataWriter {
    private SSHConnection sshConnection;

    public final static String POLICY = "policy";

    /**
     *
     */
    public JNS5GTWriter(SSHConnection sshConnection) throws ConnectionException {
        this.sshConnection = sshConnection;
        this.sshConnection.establish();
    }

    @Override
    public void set(String variableName, Object newValue) {
        try {
            switch (variableName) {
                case POLICY:
                    setPolicy((JNS5GTPolicy) newValue);
                    break;
            }
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
    }

    /**
     * There is this
     * @param variableName
     * @param newValue
     */
    public void unset(String variableName, Object newValue) {
        try {
            switch (variableName) {
                case POLICY:
                    unsetPolicy((JNS5GTPolicy) newValue);
                    break;
            }
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param policy
     * @return
     */
    public String getSetCommand(JNS5GTPolicy policy){
        // policy get action using dictionary!
        JNS5GTValuesToEnglishDictionary a = new JNS5GTValuesToEnglishDictionary();
        return String.format("set policy id %d name \"%s\" from %s to %s %s %s %s %s",
                policy.getId(), policy.getName(), policy.getSrcZone(), policy.getDstZone(),
                policy.getSrcAddress(), policy.getDstAddress(), a.getB2ADefinition(policy.getService()), policy.getAction() == 0 ? "deny" : "permit"
        );
    }

    public String getUnsetCommand(JNS5GTPolicy policy){
        // policy get action using dictionary!
        return String.format("unset policy id %d", policy.getId());
    }

    public void setPolicy(JNS5GTPolicy policy) throws ConnectionException {
        sshConnection.execute(getSetCommand(policy));
    }

    public void unsetPolicy(JNS5GTPolicy policy) throws ConnectionException {
        sshConnection.execute(getUnsetCommand(policy));
    }
}
