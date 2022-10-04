package saveroll.saveroll.typerolls;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class AttackRoll extends TypesRoll{
    public AttackRoll() {
        boosts.put("HORSE", 0);
    }

    @Override
    public int calculateRoll(Player player) {
        int plusRoll = boosts.get("DEFAULT");
        if(!player.isInsideVehicle()) return plusRoll;
        if (player.getVehicle().getType().equals(EntityType.HORSE)) plusRoll += boosts.get("HORSE");
        return plusRoll;
    }
}
