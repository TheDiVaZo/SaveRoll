package saveroll.saveroll.bonus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import saveroll.errors.NotExistMaterialException;
import saveroll.logging.Logger;
import saveroll.saveroll.importancelevelobject.BanLevelObject;
import saveroll.saveroll.importancelevelobject.ImportanceLevelObject;
import saveroll.saveroll.importancelevelobject.NeutralLevelObject;
import saveroll.saveroll.importancelevelobject.RequireLevelObject;

import java.util.*;

public class EquipmentCondition extends Condition {
    
    private static final char REQUIED_SIGN = '*';
    private static final char BAN_SIGN = '!';

    private final ArrayList<EquipmentBonus> equipmentBonuses;

    protected enum EquipmentSlotWrapper {
        FEED {
            @Override
            public boolean isEquipMaterial(PlayerInventory inventory, Material material) {
                ItemStack feed = inventory.getBoots();
                if(Objects.isNull(feed)) return false;
                return feed.getType().equals(material);
            }
        },
        CHEST {
            @Override
            public boolean isEquipMaterial(PlayerInventory inventory, Material material) {
                ItemStack chest = inventory.getChestplate();
                if(Objects.isNull(chest)) return false;
                return chest.getType().equals(material);
            }
        },
        HEAD {
            @Override
            public boolean isEquipMaterial(PlayerInventory inventory, Material material) {
                ItemStack head = inventory.getHelmet();
                if(Objects.isNull(head)) return false;
                return head.getType().equals(material);
            }
        },
        LEGS {
            @Override
            public boolean isEquipMaterial(PlayerInventory inventory, Material material) {
                ItemStack legs = inventory.getLeggings();
                if(Objects.isNull(legs)) return false;
                return legs.getType().equals(material);
            }
        },
        HAND {
            @Override
            public boolean isEquipMaterial(PlayerInventory inventory, Material material) {
                ItemStack hand = inventory.getItemInMainHand();
                return hand.getType().equals(material);
            }
        },
        OFF_HAND {
            @Override
            public boolean isEquipMaterial(PlayerInventory inventory, Material material) {
                ItemStack offHand = inventory.getItemInOffHand();
                return offHand.getType().equals(material);
            }
        },
        INVENTORY {
            @Override
            public boolean isEquipMaterial(PlayerInventory inventory, Material material) {
                for (ItemStack storageContent : inventory.getStorageContents()) {
                    if(storageContent != null && storageContent.getType().equals(material)) {
                        return true;
                    }
                }
                return false;
            }
        };

        public abstract boolean isEquipMaterial(PlayerInventory inventory, Material material);
    }

    protected static class EquipmentBonus {
        protected ArrayList<ImportanceLevelObject<Material>> items;
        protected ArrayList<ImportanceLevelObject<EquipmentSlotWrapper>> slots;
        protected int fillSlots;
        protected int additionalRoll;

        protected EquipmentBonus(ArrayList<ImportanceLevelObject<Material>> items, ArrayList<ImportanceLevelObject<EquipmentSlotWrapper>> slots, int fillSlots, int additionalRoll) {
            this.items = items;
            this.slots = slots;
            this.fillSlots = fillSlots;
            this.additionalRoll = additionalRoll;
        }
        
        private static ArrayList<ImportanceLevelObject<Material>> generateItems(@NotNull List<String> items) {
            ArrayList<ImportanceLevelObject<Material>> materialItems = new ArrayList<>();
            for (String itemName : items) {
                if(Objects.isNull(itemName)) continue;
                char signImportance = itemName.charAt(0);
                itemName = itemName.replaceFirst("^([\\!\\*])+", "");
                Material itemMaterial;
                try {
                    itemMaterial = Material.valueOf(itemName.toUpperCase(Locale.ROOT));
                } catch (IllegalArgumentException | NullPointerException exception) {
                    Logger.error("Материал "+itemName+" не является действительным! Пожалуйста, проверьте конфиг на наличие опечаток.");
                    continue;
                }
                if(signImportance == REQUIED_SIGN) materialItems.add(new RequireLevelObject<>(itemMaterial));
                else if(signImportance == BAN_SIGN) materialItems.add(new BanLevelObject<>(itemMaterial));
                else materialItems.add(new NeutralLevelObject<>(itemMaterial));
            }
            return materialItems;
        }
        private static ArrayList<ImportanceLevelObject<EquipmentSlotWrapper>> generateSlots(@NotNull List<String> slots) throws NotExistMaterialException {
            ArrayList<ImportanceLevelObject<EquipmentSlotWrapper>> equipmentWrapperSlots = new ArrayList<>();
            for (String slotName : slots) {
                if(slotName == null) continue;
                char signImportance = slotName.charAt(0);
                slotName = slotName.replaceFirst("^([\\!\\*])+", "");
                EquipmentSlotWrapper equipmentWrapperSlot = getMaterialFromString("Слота с названием "+slotName+" не существует! Проверьте корректность слова", slotName, EquipmentSlotWrapper.class);
                if(signImportance == REQUIED_SIGN) equipmentWrapperSlots.add(new RequireLevelObject<>(equipmentWrapperSlot));
                else if(signImportance == BAN_SIGN) equipmentWrapperSlots.add(new BanLevelObject<>(equipmentWrapperSlot));
                else equipmentWrapperSlots.add(new NeutralLevelObject<>(equipmentWrapperSlot));
            }
            return equipmentWrapperSlots;
        }
        
        public static EquipmentBonus generateItemBonus(@NotNull List<String> items, @NotNull List<String> slots, int fillSlots, int additionalRoll) throws NotExistMaterialException {
            return new EquipmentBonus(generateItems(items), generateSlots(slots), fillSlots, additionalRoll);
        }

        private boolean isRequiredItemInSlotForPlayer(PlayerInventory inventory, EquipmentSlotWrapper slot ,Material item) {
            return slot.isEquipMaterial(inventory, item);
        }

        public int getBonusFromPlayer(Player player) {
            PlayerInventory inventory = player.getInventory();
            int requiredMaterial = (int) items.stream().filter(ImportanceLevelObject::isRequied).count();
            int requiredSlots = (int) slots.stream().filter(ImportanceLevelObject::isRequied).count();

            int countFillRequiredMaterial = 0;
            int countFillRequiredSlots = 0;

            int countFillSlotsFromItems = 0;

            int maybeAdditionalRoll = this.additionalRoll;

            for (ImportanceLevelObject<EquipmentSlotWrapper> slot : slots) {
                for (ImportanceLevelObject<Material> item : items) {
                    if((slot.isRequied() || slot.isNeutral()) && (item.isRequied() || item.isNeutral()) && isRequiredItemInSlotForPlayer(inventory, slot.getObject(), item.getObject())) {
                        if(slot.isRequied()) countFillRequiredSlots++;
                        if(item.isRequied()) countFillRequiredMaterial++;
                        countFillSlotsFromItems++;
                        break;
                    }
                    else if((slot.isBan() || item.isBan()) && isRequiredItemInSlotForPlayer(inventory, slot.getObject(), item.getObject())) maybeAdditionalRoll = 0;
                }
            }

            if(requiredMaterial != countFillRequiredMaterial) maybeAdditionalRoll = 0;
            if(requiredSlots != countFillRequiredSlots) maybeAdditionalRoll = 0;
            if(countFillSlotsFromItems < fillSlots) maybeAdditionalRoll = 0;

            return maybeAdditionalRoll;


        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof EquipmentBonus)) return false;
            EquipmentBonus equipmentBonus = (EquipmentBonus) o;
            return fillSlots == equipmentBonus.fillSlots && additionalRoll == equipmentBonus.additionalRoll && items.equals(equipmentBonus.items) && slots.equals(equipmentBonus.slots);
        }

        @Override
        public int hashCode() {
            return Objects.hash(items, slots, fillSlots, additionalRoll);
        }
    }

    protected EquipmentCondition(ArrayList<EquipmentBonus> equipmentBonuses) {
        super("equip");
        this.equipmentBonuses = equipmentBonuses;
    }


    @Override
    public int calculateRoll(Player player) {
        if(equipmentBonuses.isEmpty()) return 0;
        return equipmentBonuses.stream().map(equipmentBonus -> equipmentBonus.getBonusFromPlayer(player)).reduce(Integer::sum).get();
    }

    public interface ConfigEquipItemsParam extends ConfigParam {
        @NotNull List<String> getItems();
        @NotNull List<String> getSlots();
        int getFillSlots();
        int getAdditionalRoll();
    }

    public static ConfigEquipItemsParam generateConfig(List<String> items, List<String> slots, int fillSlots, int additionalRoll) {
        return new ConfigEquipItemsParam() {
            @Override
            public @NotNull List<String> getItems() {
                return items;
            }

            @Override
            public @NotNull List<String> getSlots() {
                return slots;
            }

            @Override
            public int getFillSlots() {
                return fillSlots;
            }

            @Override
            public int getAdditionalRoll() {
                return additionalRoll;
            }
        };
    }

    @NotNull public static Condition generateBonus(@NotNull ConfigEquipItemsParam ... configEquipItemsParam) throws NotExistMaterialException {
        ArrayList<EquipmentBonus> equipmentBonusesGenerate = new ArrayList<>();
        EquipmentCondition bonus = new EquipmentCondition(equipmentBonusesGenerate);
        for (ConfigEquipItemsParam equipItemsParam : configEquipItemsParam) {
            EquipmentBonus equipmentBonus = EquipmentBonus.generateItemBonus(equipItemsParam.getItems(), equipItemsParam.getSlots(), equipItemsParam.getFillSlots(), equipItemsParam.getAdditionalRoll());
            equipmentBonusesGenerate.add(equipmentBonus);
        }
        return bonus;
    }

    @NotNull public static Condition generateBonus(@NotNull List<ConfigEquipItemsParam>  configEquipItemsParam) throws NotExistMaterialException {
        ArrayList<EquipmentBonus> equipmentBonusesGenerate = new ArrayList<>();
        EquipmentCondition bonus = new EquipmentCondition(equipmentBonusesGenerate);
        for (ConfigEquipItemsParam equipItemsParam : configEquipItemsParam) {
            EquipmentBonus equipmentBonus = EquipmentBonus.generateItemBonus(equipItemsParam.getItems(), equipItemsParam.getSlots(), equipItemsParam.getFillSlots(), equipItemsParam.getAdditionalRoll());
            equipmentBonusesGenerate.add(equipmentBonus);
        }
        return bonus;
    }
}
