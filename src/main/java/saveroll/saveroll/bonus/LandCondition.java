package saveroll.saveroll.bonus;

import me.angeschossen.lands.api.integration.LandsIntegration;
import me.angeschossen.lands.api.land.Land;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LandCondition extends Condition {

    private final ArrayList<LandBonus> landBonuses;
    private final LandsIntegration landsIntegration;

    protected static class LandBonus {
        protected List<Land> lands;

        public LandBonus(List<Land> lands) {
            this.lands = lands;
        }
    }

    protected LandCondition(LandsIntegration landsIntegration, ArrayList<LandBonus> landBonuses) {
        super("land");
        this.landsIntegration = landsIntegration;
        this.landBonuses = landBonuses;
    }

    @Override
    public int calculateRoll(Player player) {
        return 0;
    }
}
