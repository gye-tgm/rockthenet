package rockthenet.dictionaries;

/**
 * Each service in the JNS5GT policies have a corresponding number. This class translates between them.
 *
 * The definitions are defined in @see <a href="http://www.circitor.fr/Mibs/Html/NETSCREEN-POLICY-MIB.php">Netscreen
 * documentation</a>
 *
 * @author Gary Ye
 * @version 2014-10-29
 */
public class JNS5GTServiceToNumber extends BidirectionalDictionary<String, Integer> {
    public JNS5GTServiceToNumber() {
        a2b.put("any", 0);
        a2b.put("aol", 1);
        a2b.put("bgp", 2);
        a2b.put("ssh", 37);
        // TODO: this can be extended

        transfera2b();
    }
}
