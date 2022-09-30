package saveroll.saveroll.typerolls;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class AttackRoll extends TypesRoll{
    public AttackRoll() {
        boosts.put("HORSE", 0);
    }

    @Override
    public int calculateRoll(Player player) {
        int plusRoll = 0;
        if(player.isInsideVehicle()) {
            if (player.getVehicle().getType().equals(EntityType.HORSE)) plusRoll += boosts.get("HORSE");
        }
        return plusRoll;
    }
}
