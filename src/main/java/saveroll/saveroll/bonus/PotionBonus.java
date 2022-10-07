package saveroll.saveroll.bonus;

import org.bukkit.entity.Player;

public class PotionBonus extends Bonus{
    public PotionBonus() {
        super("potion");
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
