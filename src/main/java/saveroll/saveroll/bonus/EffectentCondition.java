package saveroll.saveroll.bonus;

import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import saveroll.errors.NotExistMaterialException;
import saveroll.errors.NotMatchPatternException;
import saveroll.saveroll.importancelevelobject.BanLevelObject;
import saveroll.saveroll.importancelevelobject.ImportanceLevelObject;
import saveroll.saveroll.importancelevelobject.NeutralLevelObject;
import saveroll.saveroll.importancelevelobject.RequireLevelObject;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EffectentCondition extends Condition {

    private static final char REQUIED_SIGN = '*';
    private static final char BAN_SIGN = '!';
    private static final String EFFECT_CONFIG_REGEX = "[*!]?([a-z_A-Z]+)\\:([0-9]{0,3})";

    private final ArrayList<EffectBonus> effectBonuses;

    public enum MinecraftNamesEffects {
        //spigot names
        FAST_DIGGING (PotionEffectType.FAST_DIGGING),
        SLOW (PotionEffectType.SLOW),
        SLOW_DIGGING (PotionEffectType.SLOW_DIGGING),
        INCREASE_DAMAGE (PotionEffectType.INCREASE_DAMAGE),
        HEAL (PotionEffectType.HEAL),
        HARM (PotionEffectType.HARM),
        JUMP (PotionEffectType.JUMP),
        CONFUSION (PotionEffectType.CONFUSION),
        DAMAGE_RESISTANCE (PotionEffectType.DAMAGE_RESISTANCE),
        UNLUCK (PotionEffectType.UNLUCK),

        //Minecraft names
        SPEED (PotionEffectType.SPEED),
        SLOWNESS (PotionEffectType.SLOW),
        HASTE (PotionEffectType.FAST_DIGGING),
        MINING_FATIGUE (PotionEffectType.SLOW_DIGGING),
        STRENGTH (PotionEffectType.INCREASE_DAMAGE),
        INSTANT_HEALTH (PotionEffectType.HEAL),
        INSTANT_DAMAGE (PotionEffectType.HARM),
        JUMP_BOOST (PotionEffectType.JUMP),
        NAUSEA (PotionEffectType.CONFUSION),
        REGENERATION (PotionEffectType.REGENERATION),
        RESISTANCE (PotionEffectType.DAMAGE_RESISTANCE),
        FIRE_RESISTANCE (PotionEffectType.FIRE_RESISTANCE),
        WATER_BREATHING (PotionEffectType.WATER_BREATHING),
        INVISIBILITY (PotionEffectType.INVISIBILITY),
        BLINDNESS (PotionEffectType.BLINDNESS),
        NIGHT_VISION (PotionEffectType.NIGHT_VISION),
        HUNGER (PotionEffectType.HUNGER),
        WEAKNESS (PotionEffectType.WEAKNESS),
        POISON (PotionEffectType.POISON),
        WITHER (PotionEffectType.WITHER),
        HEALTH_BOOST (PotionEffectType.HEALTH_BOOST),
        ABSORPTION (PotionEffectType.ABSORPTION),
        SATURATION (PotionEffectType.SATURATION),
        GLOWING (PotionEffectType.GLOWING),
        LEVITATION (PotionEffectType.LEVITATION),
        LUCK (PotionEffectType.LUCK),
        BAD_LUCK (PotionEffectType.UNLUCK),
        SLOW_FALLING (PotionEffectType.SLOW_FALLING),
        CONDUIT_POWER (PotionEffectType.CONDUIT_POWER),
        DOLPHINS_GRACE (PotionEffectType.DOLPHINS_GRACE),
        BAD_OMEN (PotionEffectType.BAD_OMEN),
        HERO_OF_THE_VILLAGE (PotionEffectType.HERO_OF_THE_VILLAGE);

        private final PotionEffectType effectTypeSpigot;
        MinecraftNamesEffects(PotionEffectType effectTypeSpigot) {
            this.effectTypeSpigot = effectTypeSpigot;
        }

        public PotionEffectType getEffectTypeSpigot() {
            return effectTypeSpigot;
        }

        public PotionEffect createEffect(int duration, int amplifier) {
            return effectTypeSpigot.createEffect(duration, amplifier);
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

        private static ArrayList<ImportanceLevelObject<PotionEffect>> generateEffects(@NotNull List<String> potionEffectNames) throws NotMatchPatternException, NotExistMaterialException {
            ArrayList<ImportanceLevelObject<PotionEffect>> generatedEffects = new ArrayList<>();
            for (String effectFormat : potionEffectNames)  {
                if(Objects.isNull(effectFormat)) continue;
                isStringMatchFormat(effectFormat,EFFECT_CONFIG_REGEX);
                char signImportance = effectFormat.charAt(0);
                String effectName = null;
                int effectLevel = 0;
                Matcher matcher = Pattern.compile(EFFECT_CONFIG_REGEX).matcher(effectFormat);
                while (matcher.find()) {
                    effectName = matcher.group(1);
                    if (effectName == null) {
                        throw new NotMatchPatternException("Эффекта "+effectName+" не существует! Проверьте конфиг на наличие опечаток.");
                    }
                    try {
                        effectLevel = Integer.parseInt(matcher.group(2));
                    } catch (NumberFormatException e) {
                        throw new NotMatchPatternException("Уровень "+effectLevel+" эффекта "+effectName+" указан некорректно. Проверьте конфиг на наличие опечаток.");
                    }
                }
                PotionEffect effect = getMaterialFromString("Эффекта "+effectName+" не существует! Проверьте конфиг на наличие опечаток." ,effectName, MinecraftNamesEffects.class).createEffect(0, effectLevel);
                if(signImportance == REQUIED_SIGN) generatedEffects.add(new RequireLevelObject<>(effect));
                else if(signImportance == BAN_SIGN) generatedEffects.add(new BanLevelObject<>(effect));
                else generatedEffects.add(new NeutralLevelObject<>(effect));
            }
            return generatedEffects;
        }

        public static EffectBonus generateEffectBonus(@NotNull List<String> potionEffectNames, int countEffects, int additionalRoll) throws NotMatchPatternException, NotExistMaterialException {

            return new EffectBonus(generateEffects(potionEffectNames), countEffects, additionalRoll);
        }

        private boolean isEffectFromPlayer(PotionEffect effect, Player player) {
            PotionEffect playerEffect = player.getPotionEffect(effect.getType());
            if(playerEffect == null) return false;
            if(effect.getType().equals(playerEffect.getType()))
                return effect.getAmplifier() <= playerEffect.getAmplifier();
            else return false;
        }

        public int getBonusFromPlayer(Player player) {
            int countEffectsInPlayer = 0;
            for (ImportanceLevelObject<PotionEffect> potionEffect : potionEffects) {
                if((potionEffect.isBan() && isEffectFromPlayer(potionEffect.getObject(), player)) || (potionEffect.isRequied() && !isEffectFromPlayer(potionEffect.getObject(), player))) return 0;
                if((potionEffect.isNeutral() || potionEffect.isRequied()) && isEffectFromPlayer(potionEffect.getObject(), player)) countEffectsInPlayer++;
            }
            if(countEffectsInPlayer < countEffects) return 0;
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

        @Override
        public String toString() {
            return "EffectBonus{" +
                    "potionEffects=" + Arrays.toString(potionEffects.toArray()) +
                    ", countEffects=" + countEffects +
                    ", additionalRoll=" + additionalRoll +
                    '}';
        }
    }
    
    public EffectentCondition(ArrayList<EffectBonus> effectBonuses) {
        super("potion");
        this.effectBonuses = effectBonuses;
    }

    @Override
    public int calculateRoll(Player player) {
        if (effectBonuses.isEmpty()) return 0;
        return effectBonuses.stream().map(effectBonus -> effectBonus.getBonusFromPlayer(player)).reduce(Integer::sum).get();
    }

    public interface ConfigPotionEffectParam extends ConfigParam {
        @NotNull List<String> getEffects();
        int getCountEffects();
        int getAdditionalRoll();
    }

    public static ConfigPotionEffectParam generateConfig(List<String> effects, int countEffects, int additionalRoll) {
        return new ConfigPotionEffectParam() {
            @Override
            public @NotNull List<String> getEffects() {
                return effects;
            }

            @Override
            public int getCountEffects() {
                return countEffects;
            }

            @Override
            public int getAdditionalRoll() {
                return additionalRoll;
            }
        };
    }

    @NotNull
    public static Condition generateBonus(@NotNull ConfigPotionEffectParam ... configPotionEffectParams) throws NotMatchPatternException, NotExistMaterialException {
        ArrayList<EffectBonus> effectPotionBonusesGenerate = new ArrayList<>();
        EffectentCondition bonus = new EffectentCondition(effectPotionBonusesGenerate);
        for (ConfigPotionEffectParam potionEffectParam : configPotionEffectParams) {
            EffectBonus effectBonus = EffectBonus.generateEffectBonus(potionEffectParam.getEffects(), potionEffectParam.getCountEffects(), potionEffectParam.getAdditionalRoll());
            effectPotionBonusesGenerate.add(effectBonus);
        }
        return bonus;
    }

    @NotNull
    public static Condition generateBonus(@NotNull List<ConfigPotionEffectParam> configPotionEffectParams) throws NotMatchPatternException, NotExistMaterialException {
        ArrayList<EffectBonus> effectPotionBonusesGenerate = new ArrayList<>();
        EffectentCondition bonus = new EffectentCondition(effectPotionBonusesGenerate);
        for (ConfigPotionEffectParam potionEffectParam : configPotionEffectParams) {
            EffectBonus effectBonus = EffectBonus.generateEffectBonus(potionEffectParam.getEffects(), potionEffectParam.getCountEffects(), potionEffectParam.getAdditionalRoll());
            effectPotionBonusesGenerate.add(effectBonus);
        }
        return bonus;
    }

    @Override
    public String toString() {
        return "EffectentBonus{" +
                "effectBonuses=" + Arrays.toString(effectBonuses.toArray()) +
                '}';
    }

    public static PotionEffectType getMaterialFromString(String errorMSG,String itemName) throws NotExistMaterialException {
        PotionEffectType object;
        try {
            object = Objects.requireNonNull(PotionEffectType.getByName(itemName.toUpperCase(Locale.ROOT)));
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new  NotExistMaterialException(errorMSG, e);
        }
        return object;
    }
}

