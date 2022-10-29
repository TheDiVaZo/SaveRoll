package saveroll.saveroll.bonus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import saveroll.logging.Logger;
import saveroll.saveroll.ImportanceLevelObject;

import java.util.*;

public class EquipmentBonus extends Bonus{
    
    private static final char REQUIED_SIGN = '*';
    private static final char BAN_SIGN = '!';

    private final ArrayList<ItemBonus> itemBonuses;

    protected static class RequiredItem extends ImportanceLevelObject<Material> {

        public RequiredItem(Material item) {
            super(item);
        }

        @Override
        public boolean isRequied() {
            return true;
        }
        
    }
    protected static class NeutralItem extends ImportanceLevelObject<Material> {

        public NeutralItem(Material item) {
            super(item);
        }

        @Override
        public boolean isNeutral() {
            return true;
        }
    }
    protected static class BanItem extends ImportanceLevelObject<Material> {

        public BanItem(Material item) {
            super(item);
        }
        
        @Override
        public boolean isBan() {
            return true;
        }
    }

    protected static class RequiredSlot extends ImportanceLevelObject<EquipmentSlotWrapper> {

        public RequiredSlot(EquipmentSlotWrapper item) {
            super(item);
        }

        @Override
        public boolean isRequied() {
            return true;
        }

    }
    protected static class NeutralSlot extends ImportanceLevelObject<EquipmentSlotWrapper> {

        public NeutralSlot(EquipmentSlotWrapper item) {
            super(item);
        }

        @Override
        public boolean isNeutral() {
            return true;
        }
    }
    protected static class BanSlot extends ImportanceLevelObject<EquipmentSlotWrapper> {

        public BanSlot(EquipmentSlotWrapper item) {
            super(item);
        }

        @Override
        public boolean isBan() {
            return true;
        }
    }

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
                    if(storageContent.getType().equals(material)) return true;
                }
                return false;
            }
        };

        public abstract boolean isEquipMaterial(PlayerInventory inventory, Material material);
    }

    protected static class ItemBonus {
        protected ArrayList<ImportanceLevelObject<Material>> items;
        protected ArrayList<ImportanceLevelObject<EquipmentSlotWrapper>> slots;
        protected int fillSlots;
        protected int additionalRoll;

        protected ItemBonus(ArrayList<ImportanceLevelObject<Material>> items, ArrayList<ImportanceLevelObject<EquipmentSlotWrapper>> slots, int fillSlots, int additionalRoll) {
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
                if(signImportance == REQUIED_SIGN) materialItems.add(new RequiredItem(itemMaterial));
                else if(signImportance == BAN_SIGN) materialItems.add(new BanItem(itemMaterial));
                else materialItems.add(new NeutralItem(itemMaterial));
            }
            return materialItems;
        }
        private static ArrayList<ImportanceLevelObject<EquipmentSlotWrapper>> generateSlots(@NotNull List<String> slots) {
            ArrayList<ImportanceLevelObject<EquipmentSlotWrapper>> equipmentWrapperSlots = new ArrayList<>();
            for (String slotName : slots) {
                if(slotName == null) continue;
                char signImportance = slotName.charAt(0);
                slotName = slotName.replaceFirst("^([\\!\\*])+", "");
                EquipmentSlotWrapper equipmentWrapperSlot;
                try {
                    equipmentWrapperSlot = EquipmentSlotWrapper.valueOf(slotName.toUpperCase(Locale.ROOT));
                } catch (IllegalArgumentException | NullPointerException exception) {
                    Logger.error("Слот "+slotName+" не является действительным! Пожалуйста, проверьте конфиг на наличие опечаток.");
                    continue;
                }
                if(signImportance == REQUIED_SIGN) equipmentWrapperSlots.add(new RequiredSlot(equipmentWrapperSlot));
                else if(signImportance == BAN_SIGN) equipmentWrapperSlots.add(new BanSlot(equipmentWrapperSlot));
                else equipmentWrapperSlots.add(new NeutralSlot(equipmentWrapperSlot));
            }
            return equipmentWrapperSlots;
        }
        
        public static ItemBonus generateItemBonus(@NotNull List<String> items, @NotNull List<String> slots, int fillSlots, int additionalRoll) {
            return new ItemBonus(generateItems(items), generateSlots(slots), fillSlots, additionalRoll);
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
                        break;
                    }
                    else if((slot.isBan() || item.isBan()) && isRequiredItemInSlotForPlayer(inventory, slot.getObject(), item.getObject())) maybeAdditionalRoll = 0;

                }
            }

            if(requiredMaterial > countFillRequiredMaterial) maybeAdditionalRoll = 0;
            if(requiredSlots > countFillRequiredSlots) maybeAdditionalRoll = 0;
            if(countFillSlotsFromItems > fillSlots) maybeAdditionalRoll = 0;

            return maybeAdditionalRoll;


        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ItemBonus)) return false;
            ItemBonus itemBonus = (ItemBonus) o;
            return fillSlots == itemBonus.fillSlots && additionalRoll == itemBonus.additionalRoll && items.equals(itemBonus.items) && slots.equals(itemBonus.slots);
        }

        @Override
        public int hashCode() {
            return Objects.hash(items, slots, fillSlots, additionalRoll);
        }
    }

    protected EquipmentBonus(ArrayList<ItemBonus> itemBonuses) {
        super("equip");
        this.itemBonuses = itemBonuses;
    }


    @Override
    public int calculateRoll(Player player) {
        return itemBonuses.stream().map(itemBonus -> itemBonus.getBonusFromPlayer(player)).reduce(Integer::sum).get();
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

    @NotNull public static Bonus generateBonus(@NotNull ConfigEquipItemsParam ... configEquipItemsParam) {
        ArrayList<ItemBonus> itemBonusesGenerate = new ArrayList<>();
        EquipmentBonus bonus = new EquipmentBonus(itemBonusesGenerate);
        for (ConfigEquipItemsParam equipItemsParam : configEquipItemsParam) {
            ItemBonus itemBonus = ItemBonus.generateItemBonus(equipItemsParam.getItems(), equipItemsParam.getSlots(), equipItemsParam.getFillSlots(), equipItemsParam.getAdditionalRoll());
            itemBonusesGenerate.add(itemBonus);
        }
        return bonus;
    }

    @NotNull public static Bonus generateBonus(@NotNull List<ConfigEquipItemsParam>  configEquipItemsParam) {
        ArrayList<ItemBonus> itemBonusesGenerate = new ArrayList<>();
        EquipmentBonus bonus = new EquipmentBonus(itemBonusesGenerate);
        for (ConfigEquipItemsParam equipItemsParam : configEquipItemsParam) {
            ItemBonus itemBonus = ItemBonus.generateItemBonus(equipItemsParam.getItems(), equipItemsParam.getSlots(), equipItemsParam.getFillSlots(), equipItemsParam.getAdditionalRoll());
            itemBonusesGenerate.add(itemBonus);
        }
        return bonus;
    }
}
