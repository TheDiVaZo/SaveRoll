package saveroll.errors;

import org.bukkit.ChatColor;

public class NotMatchPatternException extends DefectiveFieldConfigException{
    protected static final String examMessage = ChatColor.translateAlternateColorCodes('&',"&cОшибка: &e%s &eсправьте все ошибки в конфигурации и снова перезагрузите командой &b/adice reload.");

    public NotMatchPatternException(String message) {
        super(String.format(examMessage, message));
    }

    public NotMatchPatternException(String message, Throwable cause) {
        super(String.format(examMessage, message), cause);
    }

    public NotMatchPatternException(Throwable cause) {
        super(cause);
    }

    public NotMatchPatternException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(String.format(examMessage, message), cause, enableSuppression, writableStackTrace);
    }
}
