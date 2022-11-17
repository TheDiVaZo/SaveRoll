package saveroll.saveroll.bonus;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;
import saveroll.errors.NotExistMaterialException;
import saveroll.logging.Logger;

import java.util.*;

public class RiderCondition extends Condition {

    private final ArrayList<RiderBonus> riderBonuses;

    protected static class RiderBonus {
        protected ArrayList<EntityType> entities;
        protected ArrayList<Material> armors;
        protected int additionalRoll;

        public RiderBonus(ArrayList<EntityType> entities, ArrayList<Material> armors, int additionalRoll) {
            this.entities = entities;
            this.armors = armors;
            this.additionalRoll = additionalRoll;
        }

        private static ArrayList<EntityType> generateEntities(@NotNull List<String> entitiesNames) throws NotExistMaterialException {
            ArrayList<EntityType> generatedEntities = new ArrayList<>();
            for (String entityName : entitiesNames) {
                if(Objects.isNull(entityName)) continue;
                EntityType entity = getMaterialFromString("Сущность "+entityName+" не найдена! Пожалуйста, проверьте конфиг на наличие опечаток.", entityName, EntityType.class);
                generatedEntities.add(entity);
            }
            return generatedEntities;
        }
        private static ArrayList<Material> generateArmors(@NotNull List<String> armorsNames) throws NotExistMaterialException {
            Logger.debug(Arrays.toString(armorsNames.toArray(new String[0])) + " armorNames! Horse");
            ArrayList<Material> generatedArmors = new ArrayList<>();
            for (String armorName : armorsNames) {
                if(Objects.isNull(armorName)) continue;
                Material armor = getMaterialFromString("Броня "+armorName+" не существует! Проверьте слово на наличие опечаток.", armorName, Material.class);
                generatedArmors.add(armor);
            }
            return generatedArmors;
        }

        public static RiderBonus generateAnimalsBonus(@NotNull List<String> entitiesNames, @NotNull List<String> armorsNames, int additionalRoll) throws NotExistMaterialException {
            return new RiderBonus(generateEntities(entitiesNames), generateArmors(armorsNames), additionalRoll);
        }

        private boolean isEquipArmorOfEntity(Material armor, LivingEntity entity) {
            for (net.minecraft.world.item.ItemStack itemStack : ((CraftLivingEntity) entity).getHandle().getArmorItems()) {
                Material armorMaterial = CraftItemStack.asCraftMirror(itemStack).getType();
                if (armorMaterial.equals(armor)) return true;
            }
            return false;
        }

        public int getBonusFromPlayer(Player player) {
            if(!player.isInsideVehicle()) return 0;
            if(!(player.getVehicle() instanceof LivingEntity livingEntity)) return 0;
            for (EntityType entity : entities) {
                for (Material armor : armors) {
                    Logger.debug(armor + " - getBonusFromPlayer");
                    if(livingEntity.getType().equals(entity) && isEquipArmorOfEntity(armor, livingEntity)) return additionalRoll;
                }
                if(armors.isEmpty()) return additionalRoll;
            }
            return 0;
        }
    }

    protected RiderCondition(ArrayList<RiderBonus> riderBonuses) {
        super("rider");
        this.riderBonuses = riderBonuses;
    }

    @Override
    public int calculateRoll(Player player) {
        if(riderBonuses.isEmpty()) return 0;
        return riderBonuses.stream().map(riderBonus -> riderBonus.getBonusFromPlayer(player)).reduce(Integer::sum).get();
    }

    public interface ConfigRiderParam extends ConfigParam {
        @NotNull List<String> getAnimals();
        @NotNull List<String> getArmors();
        int getAdditionalRoll();
    }

    public static ConfigRiderParam generateConfig(List<String> animals, List<String> armors, int additionalRoll) {
        return new ConfigRiderParam() {
            @Override
            public @NotNull List<String> getAnimals() {
                return animals;
            }

            @Override
            public @NotNull List<String> getArmors() {
                return armors;
            }

            @Override
            public int getAdditionalRoll() {
                return additionalRoll;
            }
        };
    }

    @NotNull public static Condition generateBonus(@NotNull ConfigRiderParam...configRiderParams) throws NotExistMaterialException {
        ArrayList<RiderBonus> riderBonuses = new ArrayList<>();
        RiderCondition bonus = new RiderCondition(riderBonuses);
        for (ConfigRiderParam riderParam : configRiderParams) {
            RiderBonus riderBonus = RiderBonus.generateAnimalsBonus(riderParam.getAnimals(), riderParam.getArmors(), riderParam.getAdditionalRoll());
            riderBonuses.add(riderBonus);
        }
        return bonus;
    }

    @NotNull public static Condition generateBonus(@NotNull List<ConfigRiderParam> configRiderParams) throws NotExistMaterialException {
        ArrayList<RiderBonus> riderBonuses = new ArrayList<>();
        RiderCondition bonus = new RiderCondition(riderBonuses);
        for (ConfigRiderParam riderParam : configRiderParams) {
            RiderBonus riderBonus = RiderBonus.generateAnimalsBonus(riderParam.getAnimals(), riderParam.getArmors(), riderParam.getAdditionalRoll());
            riderBonuses.add(riderBonus);
        }
        return bonus;
    }


}
