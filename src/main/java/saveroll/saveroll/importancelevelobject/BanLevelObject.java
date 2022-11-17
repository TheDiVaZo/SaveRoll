package saveroll.saveroll.importancelevelobject;

public class BanLevelObject<T> extends ImportanceLevelObject<T>{
    public BanLevelObject(T object) {
        super(object);
    }

    @Override
    public boolean isBan() {
        return true;
    }

    @Override
    public boolean isNeutral() {
        return false;
    }

    @Override
    public boolean isRequied() {
        return false;
    }
}
