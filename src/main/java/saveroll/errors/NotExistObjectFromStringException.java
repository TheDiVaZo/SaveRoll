package saveroll.errors;

import org.bukkit.ChatColor;

public class NotExistObjectFromStringException extends DefectiveFieldConfigException {
    protected static final String consoleErrorMessage = "Ошибка в работе класса \"%s\". Объекта с названием \"%s\" не существует. Пожалуйста, проверьте правильность написания конфигурации. После проверки и редактирования, перезагрузите конфигурацию командой /adice reload";
    protected static final String consoleErrorColoredMessage = ChatColor.translateAlternateColorCodes('&',  "&cОшибка в работе класса &e\"%s\"&c. Объекта с названием &e\"%s\"&c не существует. Пожалуйста, проверьте правильность написания конфигурации. После проверки и редактирования, перезагрузите конфигурацию командой &e/adice reload");

    protected static String insertInErrorMessage(Object... objects) {
        return String.format(consoleErrorMessage, objects);
    }

    protected static String insertInErrorColoredMessage(Object... objects) {
        return String.format(consoleErrorColoredMessage, objects);
    }

    public NotExistObjectFromStringException(Object... message) {
        super(insertInErrorMessage(message));
    }

    public NotExistObjectFromStringException(Throwable cause, Object... message) {
        super(insertInErrorMessage(message), cause);
    }

    public NotExistObjectFromStringException(Throwable cause) {
        super(cause);
    }

    public NotExistObjectFromStringException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(insertInErrorMessage(message), cause, enableSuppression, writableStackTrace);
    }

}
