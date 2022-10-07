package saveroll.saveroll.bonus;

import org.bukkit.entity.Player;

public class RiderBonus extends Bonus {
    protected RiderBonus() {
        super("rider");
    }

    @Override
    public int calculateRoll(Player player) {
        return 0;
    }

    @Override
    public Bonus generateBonus() {
        return null;
    }


}
