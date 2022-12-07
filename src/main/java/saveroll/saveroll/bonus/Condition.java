package saveroll.saveroll.bonus;

import de.tr7zw.nbtapi.NBTContainer;
import de.tr7zw.nbtapi.NbtApiException;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import saveroll.errors.NotExistObjectFromStringException;
import saveroll.saveroll.importancelevelobject.BanLevelObject;
import saveroll.saveroll.importancelevelobject.ImportanceLevelObject;
import saveroll.saveroll.importancelevelobject.NeutralLevelObject;
import saveroll.saveroll.importancelevelobject.RequireLevelObject;
import saveroll.util.MatcherWrapper;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public abstract class Condition {

    protected static final MatcherWrapper REQUIED_SIGN = new MatcherWrapper(Pattern.compile("^\\*"));
    protected static final MatcherWrapper BAN_SIGN = new MatcherWrapper(Pattern.compile("^\\!"));

    public static final MatcherWrapper IMPORTANCE_FLAG = new MatcherWrapper(Pattern.compile("^([\\*\\!]?)"));

    protected static String NBTTagToString(@Nullable Object nbtTag) throws NbtApiException {
        if(Objects.isNull(nbtTag)) return null;
        return nbtTag.toString();
    }

    public static <T> void addWithImportanceLevel(String format, T object, List<ImportanceLevelObject<T>> importanceLevelObjectList) {
        if(REQUIED_SIGN.find(format)) importanceLevelObjectList.add(new RequireLevelObject<>(object));
        else if(BAN_SIGN.find(format)) importanceLevelObjectList.add(new BanLevelObject<>(object));
        else importanceLevelObjectList.add(new NeutralLevelObject<>(object));
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
            throw new NotExistObjectFromStringException("Неправельное название объекта \""+ itemName +"\". Пожалуйста, перепроверьте конфиг на наличие опечаток.");
        }
        return object;
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
