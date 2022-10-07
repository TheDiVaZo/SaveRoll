package saveroll.saveroll.bonus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import saveroll.logging.Logger;
import saveroll.saveroll.ImportanceLevelObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

public class EquipmentBonus extends Bonus{
    
    private static final char REQUIED_SIGN = '*';
    private static final char BAN_SIGN = '!';

    protected static class RequiedItem extends ImportanceLevelObject<Material> {

        public RequiedItem(Material item) {
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

    protected static class RequiedSlot extends ImportanceLevelObject<EquipmentSlotWrapper> {

        public RequiedSlot(EquipmentSlotWrapper item) {
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
            public EquipmentSlot getEquipmentSlot() {
                return EquipmentSlot.FEET;
            }
        },
        CHEST {
            @Override
            public EquipmentSlot getEquipmentSlot() {
                return EquipmentSlot.CHEST;
            }
        },
        HEAD {
            @Override
            public EquipmentSlot getEquipmentSlot() {
                return EquipmentSlot.HEAD;
            }
        },
        LEGS {
            @Override
            public EquipmentSlot getEquipmentSlot() {
                return EquipmentSlot.LEGS;
            }
        },
        HAND {
            @Override
            public EquipmentSlot getEquipmentSlot() {
                return EquipmentSlot.HAND;
            }
        },
        OFF_HAND {
            @Override
            public EquipmentSlot getEquipmentSlot() {
                return EquipmentSlot.OFF_HAND;
            }
        },
        INVENTORY {
            @Override
            public EquipmentSlot getEquipmentSlot() {
                return null;
            }
        };

        public abstract EquipmentSlot getEquipmentSlot();
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
        
        private static ArrayList<ImportanceLevelObject<Material>> generateItems(ArrayList<String> items) {
            ArrayList<ImportanceLevelObject<Material>> materialItems = new ArrayList<>();
            for (String itemName : items) {
                if(itemName == null) continue;
                char signImportance = itemName.charAt(0);
                itemName = itemName.replaceFirst("^([\\!\\*])+", "");
                Material itemMaterial;
                try {
                    itemMaterial = Material.valueOf(itemName.toUpperCase(Locale.ROOT));
                } catch (IllegalArgumentException | NullPointerException exception) {
                    Logger.error("Материал "+itemName+" не является действительным! Пожалуйста, проверьте конфиг на наличие опечаток.");
                    continue;
                }
                if(signImportance == REQUIED_SIGN) materialItems.add(new RequiedItem(itemMaterial));
                else if(signImportance == BAN_SIGN) materialItems.add(new BanItem(itemMaterial));
                else materialItems.add(new NeutralItem(itemMaterial));
            }
            return materialItems;
        }
        private static ArrayList<ImportanceLevelObject<EquipmentSlotWrapper>> generateSlots(ArrayList<String> slots) {
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
                if(signImportance == REQUIED_SIGN) equipmentWrapperSlots.add(new RequiedSlot(equipmentWrapperSlot));
                else if(signImportance == BAN_SIGN) equipmentWrapperSlots.add(new BanSlot(equipmentWrapperSlot));
                else equipmentWrapperSlots.add(new NeutralSlot(equipmentWrapperSlot));
            }
            return equipmentWrapperSlots;
        }
        
        public static ItemBonus generateItemBonus(ArrayList<String> items, ArrayList<String> slots, int fillSlots, int additionalRoll) {
            return new ItemBonus(generateItems(items), generateSlots(slots), fillSlots, additionalRoll);
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

    protected EquipmentBonus() {
        super("equip");
    }


    @Override
    public int calculateRoll(Player player) {
        return 0;
    }

    @Override
    public Bonus generateBonus() {
        return null;
    }
}
