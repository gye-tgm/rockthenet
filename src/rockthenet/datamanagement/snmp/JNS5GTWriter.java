package rockthenet.datamanagement.snmp;

import rockthenet.connections.ConnectionException;
import rockthenet.connections.ssh.SSHConnection;
import rockthenet.datamanagement.IDataWriter;
import rockthenet.dictionaries.JNS5GTServiceToNumber;
import rockthenet.firewall.jns5gt.JNS5GTPolicy;

/**
 * The JNS5GT writer handles writing the data from the given JNS5GT firewall. An SSH connection will be used
 * for that. JNS5gt has its own OS, that's why this class makes sure to choose the appropriate commands for
 * modifying the data.
 * @author Gary Ye
 */
public class JNS5GTWriter implements IDataWriter {
    public final static String POLICY = "policy";

    private SSHConnection sshConnection;

    /**
     * Constructs a new JNS5GT writer with the given ssh connection
     * @param sshConnection the not yet unestablished ssh connection
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
     * Unsets (removes) the given variable on the firewall.
     * @param variableName the name of the variable
     * @param newValue the name of the object
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
     * Returns the command for creating a new policy on the given policy.
     * @param policy the policy for which a set command should be created
     * @return the command for setting a new policy on the firewall
     */
    public static String getSetCommand(JNS5GTPolicy policy){
        // policy get action using dictionary!
        JNS5GTServiceToNumber a = new JNS5GTServiceToNumber();
        return String.format("set policy id %d name \"%s\" from %s to %s %s %s %s %s",
                policy.getId(), policy.getName(), policy.getSrcZone(), policy.getDstZone(),
                policy.getSrcAddress(), policy.getDstAddress(), a.getB2ADefinition(policy.getService()), policy.getAction() == 0 ? "deny" : "permit"
        );
    }

    /**
     * Returns the command for deleting a given policy.
     * @param policy the policy to delete
     * @return the string for deleting a policy on the firewall
     */
    public static String getUnsetCommand(JNS5GTPolicy policy){
        // policy get action using dictionary!
        return String.format("unset policy id %d", policy.getId());
    }

    /**
     * Sets the policy on the firewall.
     * @param policy the policy to set
     * @throws ConnectionException will be thrown if a connection exception occurs
     */
    public void setPolicy(JNS5GTPolicy policy) throws ConnectionException {
        sshConnection.execute(getSetCommand(policy));
    }

    /**
     * Deletes the policy on the firewall.
     * @param policy the policy to delete
     * @throws ConnectionException will be thrown if a connection exception occurs
     */
    public void unsetPolicy(JNS5GTPolicy policy) throws ConnectionException {
        sshConnection.execute(getUnsetCommand(policy));
    }
}
