package test.datamanagement.snmp;

import org.junit.Ignore;
import org.junit.Test;
import rockthenet.connections.ConnectionException;
import rockthenet.connections.ssh.SSHConnection;
import rockthenet.datamanagement.snmp.JNS5GTWriter;
import rockthenet.firewall.jns5gt.JNS5GTPolicy;

/**
 * Created by gary on 22/10/14.
 */
public class JNS5GTWriterTest {
    @Ignore
    @Test
    public void testUnset() throws ConnectionException {
        JNS5GTWriter jns5GTWriter = new JNS5GTWriter(new SSHConnection("10.0.100.10", "5ahit", "Waeng7ohch8o"));
        JNS5GTPolicy deletePolicy = new JNS5GTPolicy();
        deletePolicy.setId(1338);
        jns5GTWriter.unset(JNS5GTWriter.POLICY, deletePolicy);
    }

    @Ignore
    @Test
    public void testSet() throws ConnectionException {
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
}
