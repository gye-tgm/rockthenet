package test.datamanagement.snmp;

import org.junit.Ignore;
import org.junit.Test;
import rockthenet.connections.ConnectionException;
import rockthenet.connections.ssh.SSHConnection;
import rockthenet.datamanagement.snmp.JNS5GTWriter;
import rockthenet.firewall.jns5gt.JNS5GTPolicy;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests the JNS5GT Writer
 * @author Gary Ye
 */
public class JNS5GTWriterTest {
    /* (ignored) Tests including the real firewall */

    @Ignore
    @Test
    public void realTestUnset() throws ConnectionException {
        JNS5GTWriter jns5GTWriter = new JNS5GTWriter(new SSHConnection("10.0.100.10", "5ahit", "Waeng7ohch8o"));
        JNS5GTPolicy deletePolicy = new JNS5GTPolicy();
        deletePolicy.setId(1338);
        jns5GTWriter.unset(JNS5GTWriter.POLICY, deletePolicy);
    }

    @Ignore
    @Test
    public void realTestSet() throws ConnectionException {
        JNS5GTWriter jns5GTWriter = new JNS5GTWriter(new SSHConnection("10.0.100.10", "5ahit", "Waeng7ohch8o"));
        JNS5GTPolicy policy = new JNS5GTPolicy();
        policy.setName("Policy Space");
        policy.setId(1236);
        policy.setSrcZone("Trust");
        policy.setDstZone("Untrust");
        policy.setAction(1);
        policy.setActiveStatus(0);
        policy.setSrcAddress("Any");
        policy.setDstAddress("Any");
        policy.setService(0);
        jns5GTWriter.set(JNS5GTWriter.POLICY, policy);
    }

    /* Junit tests with mock objects */

    @Test
    public void testCRUD() throws ConnectionException {
        SSHConnection sshConnection = mock(SSHConnection.class);
        JNS5GTWriter writer = new JNS5GTWriter(sshConnection);

        JNS5GTPolicy policy = new JNS5GTPolicy();
        policy.setName("Policy Space");
        policy.setId(1236);
        policy.setSrcZone("Trust");
        policy.setDstZone("Untrust");
        policy.setAction(1);
        policy.setActiveStatus(0);
        policy.setSrcAddress("Any");
        policy.setDstAddress("Any");
        policy.setService(0);

        writer.set(JNS5GTWriter.POLICY, policy);
        writer.unset(JNS5GTWriter.POLICY, policy);
    }

    @Test
    public void testGetSetCommand() throws ConnectionException {
        JNS5GTPolicy policy = new JNS5GTPolicy();
        policy.setName("Policy Space");
        policy.setId(1236);
        policy.setSrcZone("Trust");
        policy.setDstZone("Untrust");
        policy.setAction(1);
        policy.setActiveStatus(0);
        policy.setSrcAddress("Any");
        policy.setDstAddress("Any");
        policy.setService(0);

        String returned = JNS5GTWriter.getSetCommand(policy);
        String expected = "set policy id 1236 name \"Policy Space\" from Trust to Untrust Any Any any permit";
        assertEquals(expected, returned);
    }

    @Ignore
    @Test
    public void test1000Command() throws ConnectionException {
        JNS5GTWriter jns5GTWriter = new JNS5GTWriter(new SSHConnection("10.0.100.10", "5ahit", "Waeng7ohch8o"));

        for(int i = 5; i < 100; i++) {
            JNS5GTPolicy policy = new JNS5GTPolicy();
            policy.setName("P" + i + "P");
            policy.setId(i);
            policy.setSrcZone("Trust");
            policy.setDstZone("Untrust");
            policy.setAction(1);
            policy.setActiveStatus(0);
            policy.setSrcAddress("Any");
            policy.setDstAddress("Any");
            policy.setService(0);
            jns5GTWriter.setPolicy(policy);

        }
    }
}
