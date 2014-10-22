package rockthenet.dictionaries;

/**
 * Created by gary on 08/10/14.
 */
public class JNS5GTValuesToEnglishDictionary extends BidirectionalDictionary<String, Integer> {
    public JNS5GTValuesToEnglishDictionary(){
        // From http://www.circitor.fr/Mibs/Html/NETSCREEN-POLICY-MIB.php
        a2b.put("any", 0);
        a2b.put("aol", 1);
        a2b.put("bgp", 2);
        a2b.put("ssh", 37);
        transfera2b();
    }
}
