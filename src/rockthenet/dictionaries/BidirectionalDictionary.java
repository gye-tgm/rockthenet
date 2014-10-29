package rockthenet.dictionaries;

import java.util.HashMap;
import java.util.Map;

/**
 * This abstract class offers subclasses to translate different definitions in bidirectional way.
 *
 * @author Gary Ye
 * @version 2014-10-29
 */
public abstract class BidirectionalDictionary<A, B> {
    protected Map<A, B> a2b;
    protected Map<B, A> b2a;

    /**
     * Returns the name in language B of the given string in language A.
     *
     * @param name a name in language A
     * @return the translated name in language B
     */
    public B getA2BDefinition(A name) {
        return a2b.get(name);
    }

    /**
     * Returns the name in language A of the given string in language B.
     *
     * @param name a name in language B
     * @return the translated name in language A
     */
    public A getB2ADefinition(B name) {
        return b2a.get(name);
    }

    /**
     * Constructs a bidirectional dictionary with no content in it.
     */
    protected BidirectionalDictionary() {
        a2b = new HashMap<>();
        b2a = new HashMap<>();
    }

    /**
     * Reflects the a2b map to the b2a map i.e. b2a will be returning the inverse
     * of the a2b map. b2a will return the definition of b.
     * <i>Works perfectly if the two related sets A and B are having a one to one relationship. In terms
     * of mathematics this relationship is called a bijection. </i>
     */
    protected void transfera2b() {
        for (Map.Entry<A, B> entry : a2b.entrySet())
            b2a.put(entry.getValue(), entry.getKey());
    }
}
