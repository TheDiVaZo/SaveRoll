package saveroll.saveroll.bonus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;
import saveroll.errors.NotExistMaterialException;
import saveroll.logging.Logger;

import java.util.*;

public class RiderBonus extends Bonus {

    private final ArrayList<AnimalsBonus> animalsBonuses;

    protected static class AnimalsBonus{
        protected ArrayList<EntityType> entities;
        protected ArrayList<Material> armors;
        protected int additionalRoll;

        public AnimalsBonus(ArrayList<EntityType> entities, ArrayList<Material> armors, int additionalRoll) {
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

        public static AnimalsBonus generateAnimalsBonus(@NotNull List<String> entitiesNames, @NotNull List<String> armorsNames, int additionalRoll) throws NotExistMaterialException {
            return new AnimalsBonus(generateEntities(entitiesNames), generateArmors(armorsNames), additionalRoll);
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

    protected RiderBonus(ArrayList<AnimalsBonus> animalsBonuses) {
        super("rider");
        this.animalsBonuses = animalsBonuses;
    }

    @Override
    public int calculateRoll(Player player) {
        if(animalsBonuses.isEmpty()) return 0;
        return animalsBonuses.stream().map(animalsBonus -> animalsBonus.getBonusFromPlayer(player)).reduce(Integer::sum).get();
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

    @NotNull public static Bonus generateBonus(@NotNull ConfigRiderParam...configRiderParams) throws NotExistMaterialException {
        ArrayList<AnimalsBonus> animalsBonuses = new ArrayList<>();
        RiderBonus bonus = new RiderBonus(animalsBonuses);
        for (ConfigRiderParam riderParam : configRiderParams) {
            AnimalsBonus animalsBonus = AnimalsBonus.generateAnimalsBonus(riderParam.getAnimals(), riderParam.getArmors(), riderParam.getAdditionalRoll());
            animalsBonuses.add(animalsBonus);
        }
        return bonus;
    }

    @NotNull public static Bonus generateBonus(@NotNull List<ConfigRiderParam> configRiderParams) throws NotExistMaterialException {
        ArrayList<AnimalsBonus> animalsBonuses = new ArrayList<>();
        RiderBonus bonus = new RiderBonus(animalsBonuses);
        for (ConfigRiderParam riderParam : configRiderParams) {
            AnimalsBonus animalsBonus = AnimalsBonus.generateAnimalsBonus(riderParam.getAnimals(), riderParam.getArmors(), riderParam.getAdditionalRoll());
            animalsBonuses.add(animalsBonus);
        }
        return bonus;
    }


}
