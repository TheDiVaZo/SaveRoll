package saveroll.saveroll.bonus;

import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffectTypeWrapper;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import saveroll.logging.JULHandler;
import saveroll.logging.Logger;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EffectentBonusTest {
    {
        Logger.init(new JULHandler(java.util.logging.Logger.getAnonymousLogger()));
    }
    @Test
    void calculateRoll() {
        EffectentBonus.ConfigPotionEffectParam configPotionEffectParam = new EffectentBonus.ConfigPotionEffectParam() {
            @Override
            public @NotNull ArrayList<String> getEffects() {
                return new ArrayList<>(){{add("*speed:1");add("!slowness:1");add("STRENGTH:1");add("INVISIBILITY:1");}};
            }

            @Override
            public int getCountEffects() {
                return 2;
            }

            @Override
            public int getAdditionalRoll() {
                return 2;
            }
        };

        Player player = Mockito.mock(Player.class);
        Mockito.when(player.getPotionEffect(PotionEffectType.SPEED)).thenReturn(new PotionEffect(PotionEffectType.SPEED, 1, 1));
        Mockito.when(player.getPotionEffect(PotionEffectType.SLOW)).thenReturn(new PotionEffect(PotionEffectType.SLOW, 1, 1));
        assert PotionType.INVISIBILITY.getEffectType() != null;
        Mockito.when(player.getPotionEffect(PotionType.INVISIBILITY.getEffectType())).thenReturn(new PotionEffect(PotionEffectType.INVISIBILITY, 1, 1));
        assert PotionType.STRENGTH.getEffectType() != null;
        Mockito.when(player.getPotionEffect(PotionType.STRENGTH.getEffectType())).thenReturn(new PotionEffect(PotionType.STRENGTH.getEffectType(), 1, 1));

        int result = EffectentBonus.generateBonus(configPotionEffectParam).calculateRoll(player);
        assertEquals(2, result);
    }
}