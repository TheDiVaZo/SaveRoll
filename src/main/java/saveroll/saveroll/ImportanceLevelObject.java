package saveroll.saveroll;

import org.bukkit.Material;

import java.util.Objects;

public abstract class ImportanceLevelObject<T> {
    protected T object;

    public ImportanceLevelObject(T object) {
        this.object = object;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public boolean isRequied() {
        return false;
    }

    public boolean isBan() {
        return false;
    }

    public boolean isNeutral() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImportanceLevelObject)) return false;
        ImportanceLevelObject<?> that = (ImportanceLevelObject<?>) o;
        return getObject().equals(that.getObject());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getObject());
    }
}
