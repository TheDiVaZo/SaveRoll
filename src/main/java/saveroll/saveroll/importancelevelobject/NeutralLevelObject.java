package saveroll.saveroll.importancelevelobject;

public class NeutralLevelObject<T> extends ImportanceLevelObject<T>{
    public NeutralLevelObject(T object) {
        super(object);
    }

    @Override
    public boolean isBan() {
        return false;
    }

    @Override
    public boolean isNeutral() {
        return true;
    }

    @Override
    public boolean isRequied() {
        return false;
    }
}
