package saveroll.saveroll.bonus;

import org.bukkit.entity.*;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import saveroll.logging.Logger;
import saveroll.saveroll.ImportanceLevelObject;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EffectentBonus extends Bonus{

    private static final char REQUIED_SIGN = '*';
    private static final char BAN_SIGN = '!';
    private static final String EFFECT_CONFIG_REGEX = "[*!]?([a-z_A-Z]+)\\:([1-9]{0,3})";

    private final ArrayList<EffectBonus> effectBonuses;

    protected static class RequiredEffect extends ImportanceLevelObject<PotionEffect> {

        public RequiredEffect(PotionEffect effectType) {
            super(effectType);
        }

        @Override
        public boolean isRequied() {
            return true;
        }

    }
    protected static class NeutralEffect extends ImportanceLevelObject<PotionEffect> {

        public NeutralEffect(PotionEffect effectType) {
            super(effectType);
        }

        @Override
        public boolean isNeutral() {
            return true;
        }

    }
    protected static class BanEffect extends ImportanceLevelObject<PotionEffect> {

        public BanEffect(PotionEffect effectType) {
            super(effectType);
        }

        @Override
        public boolean isBan() {
            return true;
        }
    }

    protected static class EffectBonus {
        protected ArrayList<ImportanceLevelObject<PotionEffect>> potionEffects;
        protected int countEffects;
        protected int additionalRoll;

        public EffectBonus(ArrayList<ImportanceLevelObject<PotionEffect>> potionEffects, int countEffects, int additionalRoll) {
            this.potionEffects = potionEffects;
            this.countEffects = countEffects;
            this.additionalRoll = additionalRoll;
        }

        private static ArrayList<ImportanceLevelObject<PotionEffect>> generateEffects(ArrayList<String> potionEffectNames) {
            ArrayList<ImportanceLevelObject<PotionEffect>> generatedEffects = new ArrayList<>();
            for (String effectFormat : potionEffectNames) {
                if(Objects.isNull(effectFormat)) continue;
                if(!effectFormat.matches(EFFECT_CONFIG_REGEX)) {
                    Logger.error(effectFormat + " эффект написан не по форме. Пожалуйста, проверьте эффект на наличие опечаток");
                    continue;
                }
                char signImportance = effectFormat.charAt(0);
                String effectName = null;
                int effectLevel = 0;
                Matcher matcher = Pattern.compile(EFFECT_CONFIG_REGEX).matcher(effectFormat);
                while (matcher.find()) {
                    effectName = matcher.group(1);
                    if (effectName == null) {
                        Logger.error("имя " + effectName + " указано неккоректно. Проверьте уровень эффекта на наличие опечаток");
                        continue;
                    }
                    try {
                        effectLevel = Integer.parseInt(matcher.group(2));
                    } catch (NumberFormatException e) {
                        Logger.error("Уровень "+effectLevel+" эффекта "+effectName+" указан неккоректно. Проверьте уровень эффекта на наличие опечаток");
                        continue;
                    }
                }
                PotionEffect effectMinecraft;
                PotionType effectType;
                try {
                    effectType = PotionType.valueOf(effectName.toUpperCase(Locale.ROOT));
                }
                catch(IllegalArgumentException | NullPointerException e) {
                    Logger.error("Эффект " + effectName + " не является действительным! Пожалуйста, проверьте конфиг на наличие опечаток.");
                    continue;
                }
                PotionEffectType potionEffectType = effectType.getEffectType();
                if(potionEffectType == null) {
                    Logger.error("Эффект "+effectName+" есть, но зелья для него не сущетсвует. Пожалуйста, используйте эффекты, которые можно вызвать зельем");
                    continue;
                }
                effectMinecraft = new PotionEffect(potionEffectType, 0, effectLevel);
                if(signImportance == REQUIED_SIGN) generatedEffects.add(new RequiredEffect(effectMinecraft));
                else if(signImportance == BAN_SIGN) generatedEffects.add(new BanEffect(effectMinecraft));
                else generatedEffects.add(new NeutralEffect(effectMinecraft));
            }
            return generatedEffects;
        }

        public static EffectBonus generateEffectBonus(ArrayList<String> potionEffectNames, int countEffects, int additionalRoll) {
            return new EffectBonus(generateEffects(potionEffectNames), countEffects, additionalRoll);
        }

        private boolean isEffectFromPlayer(PotionEffect effect, Player player) {
            PotionEffect playerEffect = player.getPotionEffect(effect.getType());
            if(playerEffect == null) return false;
            return effect.getAmplifier() >= playerEffect.getAmplifier();
        }

        public int getBonusFromPlayer(Player player) {
            int countEffectsInPlayerEffect = 0;
            for (ImportanceLevelObject<PotionEffect> potionEffect : potionEffects) {
                if((potionEffect.isBan() && isEffectFromPlayer(potionEffect.getObject(), player)) || (potionEffect.isRequied() && !isEffectFromPlayer(potionEffect.getObject(), player))) return 0;
                if((potionEffect.isNeutral() || potionEffect.isRequied()) && isEffectFromPlayer(potionEffect.getObject(), player)) countEffectsInPlayerEffect++;
            }
            if(countEffectsInPlayerEffect < countEffects) return 0;
            return additionalRoll;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof EffectBonus that)) return false;
            return countEffects == that.countEffects && additionalRoll == that.additionalRoll && potionEffects.equals(that.potionEffects);
        }

        @Override
        public int hashCode() {
            return Objects.hash(potionEffects, countEffects, additionalRoll);
        }
    }
    
    public EffectentBonus(ArrayList<EffectBonus> effectBonuses) {
        super("potion");
        this.effectBonuses = effectBonuses;
    }

    @Override
    public int calculateRoll(Player player) {
        return effectBonuses.stream().map(effectBonus -> effectBonus.getBonusFromPlayer(player)).reduce(Integer::sum).get();
    }

    public interface ConfigPotionEffectParam {
        @NotNull ArrayList<String> getEffects();
        int getCountEffects();
        int getAdditionalRoll();
    }

    @NotNull
    public static Bonus generateBonus(@NotNull ConfigPotionEffectParam ... configPotionEffectParams) {
        ArrayList<EffectBonus> effectPotionBonusesGenerate = new ArrayList<>();
        EffectentBonus bonus = new EffectentBonus(effectPotionBonusesGenerate);
        for (ConfigPotionEffectParam potionEffectParam : configPotionEffectParams) {
            EffectBonus effectBonus = EffectBonus.generateEffectBonus(potionEffectParam.getEffects(), potionEffectParam.getCountEffects(), potionEffectParam.getAdditionalRoll());
            effectPotionBonusesGenerate.add(effectBonus);
        }
        return bonus;
    }
}

