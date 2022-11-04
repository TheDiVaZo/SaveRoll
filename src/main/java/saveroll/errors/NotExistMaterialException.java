package saveroll.errors;

import org.bukkit.ChatColor;

public class NotExistMaterialException extends DefectiveFieldConfigException {
    protected static final String examMessage = ChatColor.translateAlternateColorCodes('&',"&cОшибка: &e%s &eИсправьте все ошибки в конфигурации, и перезагрузите командой &b/adice reload.");


    public NotExistMaterialException(String message) {
        super(String.format(examMessage, message));
    }

    public NotExistMaterialException(String message, Throwable cause) {
        super(String.format(examMessage, message), cause);
    }

    public NotExistMaterialException(Throwable cause) {
        super(cause);
    }

    public NotExistMaterialException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(String.format(examMessage, message), cause, enableSuppression, writableStackTrace);
    }
}
