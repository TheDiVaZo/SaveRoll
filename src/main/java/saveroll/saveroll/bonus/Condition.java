package saveroll.saveroll.bonus;

import org.bukkit.entity.Player;
import saveroll.errors.NotExistMaterialException;
import saveroll.errors.NotMatchPatternException;

import java.util.Locale;

public abstract class Condition {
    public interface plagEnum<T> {
        T getByName(String string);
    }

    protected final String name;

    protected interface ConfigParam {}

    protected Condition(String name) {
        this.name = name;
    }

    public abstract int calculateRoll(Player player);

    public static <T extends Enum<T>> T getMaterialFromString(String errorMSG,String itemName, Class<T> enumClass) throws NotExistMaterialException {
        T object;
        try {
            object = Enum.valueOf(enumClass,itemName.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new  NotExistMaterialException(errorMSG, e);
        }
        return object;
    }

    public static void isStringMatchFormat( String string, String regex ) throws NotMatchPatternException {
        if(!string.matches(regex)) {
            throw new NotMatchPatternException(string + " не соответствует формату "+regex+". Проверьте слово на наличие опечаток.");
        }
    }


    public String getName() {
        return name;
    }
}
