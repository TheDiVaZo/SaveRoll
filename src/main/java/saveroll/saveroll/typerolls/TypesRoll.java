package saveroll.saveroll.typerolls;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public abstract class TypesRoll {
    protected Map<String, Integer> boosts = new HashMap<String, Integer>(){{put("DEFAULT", 0);}};

    public boolean isValidedBoost(String boost) {
        if(boosts.containsKey(boost)) return true;
        else return false;
    }

    public void setBoost(String boostName, int boostRoll) {
        if(isValidedBoost(boostName)) boosts.put(boostName, boostRoll);
        else throw new IllegalArgumentException("There is no such boost for name "+boostName);
    }

    public Integer getBoost(String boostName) {
        if(isValidedBoost(boostName)) return boosts.get(boostName);
        else throw new IllegalArgumentException("There is no such boost for name "+boostName);
    }

    public abstract int calculateRoll(Player player);
}
