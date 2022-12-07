package saveroll.saveroll.bonus;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTContainer;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NbtApiException;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import saveroll.errors.NotExistObjectFromStringException;
import saveroll.logging.Logger;
import saveroll.saveroll.importancelevelobject.BanLevelObject;
import saveroll.saveroll.importancelevelobject.ImportanceLevelObject;
import saveroll.saveroll.importancelevelobject.NeutralLevelObject;
import saveroll.saveroll.importancelevelobject.RequireLevelObject;
import saveroll.util.MatcherWrapper;

import java.util.*;
import java.util.regex.Pattern;

public class EquipmentCondition extends Condition {

    public static final MatcherWrapper NBT_JSON_FLAG = new MatcherWrapper(Pattern.compile("(\\{.*\\})$"));
    public static final MatcherWrapper ITEM_NAME = new MatcherWrapper(Pattern.compile("([a-zA-Z_]+)"));
    public static final MatcherWrapper SLOT_NAME = new MatcherWrapper(Pattern.compile("([a-zA-Z_0-9]+)"));

    private final ArrayList<EquipmentBonus> equipmentBonuses;

    protected enum EquipmentSlotWrapper {
        FEED {
            @Override
            public boolean isEquipMaterial(PlayerInventory inventory, MaterialWrapper material) {
                return isEquip(inventory.getBoots(), material);
            }
        },
        CHEST {
            @Override
            public boolean isEquipMaterial(PlayerInventory inventory, MaterialWrapper material) {
                return isEquip(inventory.getChestplate(), material);
            }
        },
        HEAD {
            @Override
            public boolean isEquipMaterial(PlayerInventory inventory, MaterialWrapper material) {
                return isEquip(inventory.getHelmet(), material);
            }
        },
        LEGS {
            @Override
            public boolean isEquipMaterial(PlayerInventory inventory, MaterialWrapper material) {
                return isEquip(inventory.getLeggings(), material);
            }
        },
        HAND {
            @Override
            public boolean isEquipMaterial(PlayerInventory inventory, MaterialWrapper material) {
                return isEquip(inventory.getItemInMainHand(), material);
            }
        },
        OFF_HAND {
            @Override
            public boolean isEquipMaterial(PlayerInventory inventory, MaterialWrapper material) {
                return isEquip(inventory.getItemInOffHand(), material);
            }
        },
        INVENTORY {
            @Override
            public boolean isEquipMaterial(PlayerInventory inventory, MaterialWrapper material) {
                for (ItemStack storageContent : inventory.getStorageContents()) {
                    if(isEquip(storageContent, material)) {
                        return true;
                    }
                }
                return false;
            }
        };

        public abstract boolean isEquipMaterial(PlayerInventory inventory, MaterialWrapper material);

        protected boolean isEquip(ItemStack ItemInSlot, MaterialWrapper materialWrapper) {
            return materialWrapper.equalsItem(ItemInSlot);
        }
    }

    protected static class MaterialWrapper {
        /*
            1. iron_helmet{} - пустой нбт тег.
            2. iron_helmet - осутствие нбт тегов.

            1. Если у предмета нет нбт тегов, значит сравнение идет только по типу материала
            2. иначе если у него путсой нбт тег или другой, сравнение идет и по типу материала, и по нбт тегу

            1. данный класс не изменяемый. Если один раз назначили, значит все.
         */

        private final String nbtItem;
        private final Material material;


        public MaterialWrapper(@NotNull Material material,@Nullable String nbtString) throws NbtApiException {
            if(Objects.isNull(nbtString)) this.nbtItem = null;
            else {
                this.nbtItem = new NBTContainer(nbtString).getCompound().toString();
            }
            this.material = material;
        }

        public boolean equalsMaterial(@NotNull Material material) {
            return this.material.equals(material);
        }

        public boolean equalsNBT(@Nullable String nbtItemOther) {
            /*
            ? - random NBT tags
                Config   |  ItemInGame
                potion and potion{?} - true
                potion and potion - true
                potion{} and potion - true
                potion{?} and potion - false
                potion{} and potion{?} - false
                potion{?} and potion{?} - true or false
             */
            if(Objects.isNull(nbtItem)) return true;
            else if(Objects.toString(nbtItem,"").equals("{}") && Objects.isNull(nbtItemOther)) return true;
            else if(Objects.isNull(nbtItemOther)) return false;
            else if(Objects.toString(nbtItem,"").equals("{}")) return false;
            else return Objects.equals(nbtItemOther,nbtItem);
        }

        @Override
        public int hashCode() {
            return Objects.hash(nbtItem, material);
        }

        public boolean equalsItem(@NotNull ItemStack itemStack) {
            Logger.debug(Objects.toString(nbtItem, "nbtItemThis - null"));
            Logger.debug(Objects.toString(NBTItem.convertItemtoNBT(itemStack).getCompound("tag"), "nbtItemItemStack - null"));
            NBTCompound compoud = NBTItem.convertItemtoNBT(itemStack).getCompound("tag");
            return equalsMaterial(itemStack.getType()) && equalsNBT(NBTTagToString(compoud));
        }
    }

    protected static class EquipmentBonus {
        protected ArrayList<ImportanceLevelObject<MaterialWrapper>> items;
        protected ArrayList<ImportanceLevelObject<EquipmentSlotWrapper>> slots;
        protected int fillSlots;
        protected int additionalRoll;

        protected EquipmentBonus(ArrayList<ImportanceLevelObject<MaterialWrapper>> items, ArrayList<ImportanceLevelObject<EquipmentSlotWrapper>> slots, int fillSlots, int additionalRoll) {
            this.items = items;
            this.slots = slots;
            this.fillSlots = fillSlots;
            this.additionalRoll = additionalRoll;
        }
        
        private static ArrayList<ImportanceLevelObject<MaterialWrapper>> generateItems(@NotNull List<String> items) throws NotExistObjectFromStringException {
            ArrayList<ImportanceLevelObject<MaterialWrapper>> materialItems = new ArrayList<>();
            for (String itemFormat : items) {
                if(Objects.isNull(itemFormat)) continue;
                Optional<String> NBTJson = Optional.ofNullable(NBT_JSON_FLAG.matchOne(itemFormat)); //
                Optional<String> itemName = Optional.ofNullable(ITEM_NAME.matchOne(itemFormat)); //
                MaterialWrapper itemMaterial;
                try {
                    Logger.debug("ItemFormat: "+itemFormat);
                    Logger.debug("ItemName: "+itemName.orElse("null"));
                    Logger.debug("NBTJson: "+NBTJson.orElse("null"));
                    itemMaterial = new MaterialWrapper(Material.valueOf(itemName.orElse("").toUpperCase(Locale.ROOT)), NBTJson.orElse(null));
                } catch (IllegalArgumentException | NullPointerException exception) {
                    throw new NotExistObjectFromStringException(exception, "Материал \""+itemFormat+"\" не является действительным! Пожалуйста, проверьте конфиг на наличие опечаток.");
                } catch (NbtApiException e) {
                    throw new NotExistObjectFromStringException(e, "NBT тэг \""+NBTJson.orElse("null")+"\" предмета \""+itemName.orElse("null")+"\" не является действительным. Проверьте конфиг на наличие опечаток!");
                }
                addWithImportanceLevel(itemFormat, itemMaterial, materialItems);
            }
            return materialItems;
        }
        private static ArrayList<ImportanceLevelObject<EquipmentSlotWrapper>> generateSlots(@NotNull List<String> slots) throws NotExistObjectFromStringException {
            ArrayList<ImportanceLevelObject<EquipmentSlotWrapper>> equipmentWrapperSlots = new ArrayList<>();
            for (String slotData : slots) {
                if(slotData == null) continue;
                Optional<String> slotName = Optional.ofNullable(SLOT_NAME.matchOne(slotData));
                EquipmentSlotWrapper equipmentWrapperSlot = getObjectFromString(slotName.orElse(""), EquipmentSlotWrapper.class);
                if(REQUIED_SIGN.find(slotData)) equipmentWrapperSlots.add(new RequireLevelObject<>(equipmentWrapperSlot));
                else if(BAN_SIGN.find(slotData)) equipmentWrapperSlots.add(new BanLevelObject<>(equipmentWrapperSlot));
                else equipmentWrapperSlots.add(new NeutralLevelObject<>(equipmentWrapperSlot));
            }
            return equipmentWrapperSlots;
        }
        
        public static EquipmentBonus generateItemBonus(@NotNull List<String> items, @NotNull List<String> slots, int fillSlots, int additionalRoll) throws NotExistObjectFromStringException {
            return new EquipmentBonus(generateItems(items), generateSlots(slots), fillSlots, additionalRoll);
        }

        private boolean isRequiredItemInSlotForPlayer(PlayerInventory inventory, EquipmentSlotWrapper slot ,MaterialWrapper item) {
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
                for (ImportanceLevelObject<MaterialWrapper> item : items) {
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

    @NotNull public static Condition generateBonus(@NotNull ConfigEquipItemsParam ... configEquipItemsParam) throws NotExistObjectFromStringException {
        ArrayList<EquipmentBonus> equipmentBonusesGenerate = new ArrayList<>();
        EquipmentCondition bonus = new EquipmentCondition(equipmentBonusesGenerate);
        for (ConfigEquipItemsParam equipItemsParam : configEquipItemsParam) {
            EquipmentBonus equipmentBonus = EquipmentBonus.generateItemBonus(equipItemsParam.getItems(), equipItemsParam.getSlots(), equipItemsParam.getFillSlots(), equipItemsParam.getAdditionalRoll());
            equipmentBonusesGenerate.add(equipmentBonus);
        }
        return bonus;
    }

    @NotNull public static Condition generateBonus(@NotNull List<ConfigEquipItemsParam>  configEquipItemsParam) throws NotExistObjectFromStringException {
        ArrayList<EquipmentBonus> equipmentBonusesGenerate = new ArrayList<>();
        EquipmentCondition bonus = new EquipmentCondition(equipmentBonusesGenerate);
        for (ConfigEquipItemsParam equipItemsParam : configEquipItemsParam) {
            EquipmentBonus equipmentBonus = EquipmentBonus.generateItemBonus(equipItemsParam.getItems(), equipItemsParam.getSlots(), equipItemsParam.getFillSlots(), equipItemsParam.getAdditionalRoll());
            equipmentBonusesGenerate.add(equipmentBonus);
        }
        return bonus;
    }
}
