package saveroll.saveroll.datebase;

import java.util.UUID;

public abstract class DateBaseManager {

    public abstract void setRollForPlayer(UUID player, String bonusName, String bonusRoll);

    public abstract int getRollForPlayer(UUID player, String bonusName);


}
