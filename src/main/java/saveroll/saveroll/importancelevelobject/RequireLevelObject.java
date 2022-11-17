package saveroll.saveroll.importancelevelobject;

public class RequireLevelObject<T> extends ImportanceLevelObject<T>{
    public RequireLevelObject(T object) {
        super(object);
    }

    @Override
    public boolean isBan() {
        return false;
    }

    @Override
    public boolean isNeutral() {
        return false;
    }

    @Override
    public boolean isRequied() {
        return true;
    }
}
