package saveroll.saveroll.bonus;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import saveroll.errors.NotExistObjectFromStringException;
import saveroll.errors.NotMatchPatternException;
import saveroll.saveroll.importancelevelobject.ImportanceLevelObject;

import java.util.List;
import java.util.Locale;

public abstract class Condition {

    protected static final char REQUIED_SIGN = '*';
    protected static final char BAN_SIGN = '!';

    private static final String IMPORTANCE_FLAG = "^([*!]?)";
    public interface plagEnum<T> {
        T getByName(String string);
    }

    protected final String name;

    protected interface ConfigParam {}

    protected Condition(String name) {
        this.name = name;
    }

    public abstract int calculateRoll(Player player);

    public static <T extends Enum<T>> T getObjectFromString(String itemName, Class<T> enumClass) throws NotExistObjectFromStringException {
        T object;
        try {
            object = Enum.valueOf(enumClass,itemName.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotExistObjectFromStringException(itemName);
        }
        return object;
    }

    public static void isStringMatchFormat( String string, String regex ) throws NotMatchPatternException {
        if(!string.matches(regex)) {
            throw new NotMatchPatternException(string + " не соответствует формату "+regex+". Проверьте слово на наличие опечаток.");
        }
    }

    @FunctionalInterface
    interface CallbackCondition<T> {
        T run(String objectString);
    }

//    protected static <T> void generateObjectFromText(@NotNull List<String> objectsInString, CallbackCondition<T> callbackCondition, @NotNull List<ImportanceLevelObject<T>> resultObject) {
//        for (String stringObject : objectsInString) {
//            if(stringObject == null) continue;
//
//        }
//    }
    public String getName() {
        return name;
    }
}
