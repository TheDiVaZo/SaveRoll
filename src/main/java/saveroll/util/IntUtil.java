package saveroll.util;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class IntUtil {
    @Nullable
    public static Integer toInt(@Nullable String number) {
        try {
            return Integer.parseInt(Optional.ofNullable(number).orElse(""));
        } catch (NumberFormatException | NullPointerException e) {
            return null;
        }
    }
}
