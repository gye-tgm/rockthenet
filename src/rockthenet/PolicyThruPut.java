package rockthenet;

import java.util.HashMap;

/**
 * Created by gary on 05/10/14.
 */
public class PolicyThruPut {
    private PolicyThruPut(String name) {
        this.name = name;
    }

    String name;
    HashMap<Integer, Integer> data;

    @Override
    public boolean equals(Object o) {
        PolicyThruPut p = (PolicyThruPut)o;
        return name.equals(p.getName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

