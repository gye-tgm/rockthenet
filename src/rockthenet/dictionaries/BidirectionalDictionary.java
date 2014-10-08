package rockthenet.dictionaries;

import java.util.HashMap;
import java.util.Map;

/**
 * The bidirectional
 * @author Gary Ye
 */
public abstract class BidirectionalDictionary<A, B> {
    protected Map<A, B> a2b;
    protected Map<B, A> b2a;
    protected B getA2BDefinition(A name){
        return a2b.get(name);
    }
    protected A getB2ADefinition(B name){
        return b2a.get(name);
    }
    protected BidirectionalDictionary() {
        a2b = new HashMap<>();
        b2a = new HashMap<>();
    }
    /**
     * Reflects the a2b map to the b2a map i.e. b2a will be returning the inverse
     * of the a2b map. b2a will return the definition of b.
     * <i>Works perfectly if the two related sets A and B are having a one to one relationship. In terms
     * of mathematics a bijection. </i>
     */
    protected void transfera2b() {
        for(Map.Entry<A, B> entry : a2b.entrySet())
            b2a.put(entry.getValue(), entry.getKey());
    }
}
