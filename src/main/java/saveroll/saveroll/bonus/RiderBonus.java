package saveroll.saveroll.bonus;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import saveroll.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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

        private static ArrayList<EntityType> generateEntities(@NotNull List<String> entitiesNames) {
            ArrayList<EntityType> generatedEntities = new ArrayList<>();
            for (String entityName : entitiesNames) {
                if(Objects.isNull(entityName)) continue;
                EntityType entity;
                try {
                    entity = EntityType.valueOf(entityName.toUpperCase(Locale.ROOT));
                } catch (IllegalArgumentException | NullPointerException e) {
                    Logger.error("Сущность "+entityName+" не найдена! Пожалуйста, проверьте конфиг на наличие опечаток.");
                    continue;
                }
                generatedEntities.add(entity);
            }
            return generatedEntities;
        }
        private static ArrayList<Material> generateArmors(@NotNull List<String> armorsNames) {
            ArrayList<Material> generatedArmors = new ArrayList<>();
            for (String armorName : armorsNames) {
                if(Objects.isNull(armorName)) continue;
                Material armor;
                try {
                    armor = Material.valueOf(armorName.toUpperCase(Locale.ROOT));
                } catch (IllegalArgumentException | NullPointerException e) {
                    Logger.error("Броня "+armorName+" не существует. Проверьте конфиг на наличие опечаток.");
                    continue;
                }
                generatedArmors.add(armor);
            }
            return generatedArmors;
        }

        public static AnimalsBonus generateRiderBonus(@NotNull List<String> entitiesNames, @NotNull List<String> armorsNames, int additionalRoll) {
            return new AnimalsBonus(generateEntities(entitiesNames), generateArmors(armorsNames), additionalRoll);
        }

        private boolean isEquipArmorOfEntity(Material armor, Horse entity) {
            ItemStack armorHorse = entity.getInventory().getArmor();
            if(armorHorse != null) {
                return armorHorse.getType().equals(armor);
            }
            else return false;
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
            if(armors.isEmpty()) return additionalRoll;
            for (Material armor : armors) {
                for (EntityType entity : entities) {
                    if(livingEntity.getType().equals(entity) && isEquipArmorOfEntity(armor, livingEntity)) return additionalRoll;
                }
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

    @NotNull public static Bonus generateBonus(@NotNull ConfigRiderParam...configRiderParams) {
        ArrayList<AnimalsBonus> animalsBonuses = new ArrayList<>();
        RiderBonus bonus = new RiderBonus(animalsBonuses);
        for (ConfigRiderParam riderParam : configRiderParams) {
            AnimalsBonus animalsBonus = AnimalsBonus.generateRiderBonus(riderParam.getAnimals(), riderParam.getArmors(), riderParam.getAdditionalRoll());
            animalsBonuses.add(animalsBonus);
        }
        return bonus;
    }

    @NotNull public static Bonus generateBonus(@NotNull List<ConfigRiderParam> configRiderParams) {
        ArrayList<AnimalsBonus> animalsBonuses = new ArrayList<>();
        RiderBonus bonus = new RiderBonus(animalsBonuses);
        for (ConfigRiderParam riderParam : configRiderParams) {
            AnimalsBonus animalsBonus = AnimalsBonus.generateRiderBonus(riderParam.getAnimals(), riderParam.getArmors(), riderParam.getAdditionalRoll());
            animalsBonuses.add(animalsBonus);
        }
        return bonus;
    }


}
