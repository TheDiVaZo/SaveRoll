package saveroll.errors;

public abstract class DefectiveFieldConfigException extends Exception{
    protected DefectiveFieldConfigException(String message) {
        super(message);
    }

    protected DefectiveFieldConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    protected DefectiveFieldConfigException(Throwable cause) {
        super(cause);
    }

    protected DefectiveFieldConfigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
