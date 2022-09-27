package saveroll.saveroll.config.params;

import org.bukkit.entity.Player;

import java.util.HashMap;

public abstract class CalcParam {

    protected final static HashMap<String, String> values = new HashMap<>();

    public void updateValue(HashMap<String, String> newValues) {
        for (String key : newValues.keySet()) {
            if(values.containsKey(key)) values.put(key,newValues.get(key));
        }
    }

    public abstract float calcValue(Player player);

    public static String getName() {
        return null;
    }
}
