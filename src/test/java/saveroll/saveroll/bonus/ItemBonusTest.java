package saveroll.saveroll.bonus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import saveroll.logging.JULHandler;
import saveroll.logging.Logger;
import saveroll.saveroll.ImportanceLevelObject;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@PrepareForTest(EquipmentBonus.EquipmentSlotWrapper.class)
class ItemBonusTest {
    {
        Logger.init(new JULHandler(java.util.logging.Logger.getAnonymousLogger()));
    }
    @Test
    void generateBonus() throws Exception {
        EquipmentBonus.ConfigEquipItemsParam configEquipArmors = new EquipmentBonus.ConfigEquipItemsParam() {
            @Override
            public @NotNull List<String> getItems() {
                return new ArrayList<>(){{add("DIAMOND_HELMET"); add("DIAMOND_BOOTS"); add("DIAMOND_CHESTPLATE"); add("LEATHER_CHESTPLATE");}};
            }

            @Override
            public @NotNull List<String> getSlots() {
                return new ArrayList<>(){{add("*HEAD"); add("*FEED"); add("*CHEST");}};
            }

            @Override
            public int getFillSlots() {
                return 2;
            }

            @Override
            public int getAdditionalRoll() {
                return 3;
            }
        };

        EquipmentBonus.ConfigEquipItemsParam configEquipShield = new EquipmentBonus.ConfigEquipItemsParam() {
            @Override
            public @NotNull List<String> getItems() {
                return new ArrayList<>(){{add("*SHIELD");}};
            }

            @Override
            public @NotNull List<String> getSlots() {
                return new ArrayList<>(){{add("HAND"); add("OFF_HAND");}};
            }

            @Override
            public int getFillSlots() {
                return 1;
            }

            @Override
            public int getAdditionalRoll() {
                return 1;
            }
        };

        Bonus equipmentBonus = EquipmentBonus.generateBonus(configEquipArmors, configEquipShield);

        Player player = Mockito.mock(Player.class);
        PlayerInventory inventory = Mockito.mock(PlayerInventory.class);

        Mockito.when(player.getInventory()).thenReturn(inventory);

        ItemStack helmet = Mockito.mock(ItemStack.class);
        Mockito.when(helmet.getType()).thenReturn(Material.DIAMOND_HELMET);
        ItemStack boots = Mockito.mock(ItemStack.class);
        Mockito.when(boots.getType()).thenReturn(Material.DIAMOND_BOOTS);
        ItemStack chest = Mockito.mock(ItemStack.class);
        Mockito.when(chest.getType()).thenReturn(Material.LEATHER_CHESTPLATE);
        ItemStack legs = Mockito.mock(ItemStack.class);
        Mockito.when(legs.getType()).thenReturn(Material.AIR);
        ItemStack hand = Mockito.mock(ItemStack.class);
        Mockito.when(hand.getType()).thenReturn(Material.AIR);
        ItemStack off_hand = Mockito.mock(ItemStack.class);
        Mockito.when(off_hand.getType()).thenReturn(Material.AIR);

        Mockito.when(inventory.getBoots()).thenReturn(boots);
        Mockito.when(inventory.getHelmet()).thenReturn(helmet);
        Mockito.when(inventory.getChestplate()).thenReturn(chest);
        Mockito.when(inventory.getLeggings()).thenReturn(legs);
        Mockito.when(inventory.getItemInMainHand()).thenReturn(hand);
        Mockito.when(inventory.getItemInOffHand()).thenReturn(off_hand);

        assertEquals(3, equipmentBonus.calculateRoll(player));
    }

    @Test
    void getBonusFromPlayer4() throws Exception {
        ArrayList<String> items = new ArrayList<>(){{add("LEATHER_HELMET"); add("LEATHER_CHESTPLATE");}};
        ArrayList<String> slots = new ArrayList<>(){{add("HEAD"); add("CHEST");}};
        EquipmentBonus.ItemBonus itemBonus = EquipmentBonus.ItemBonus.generateItemBonus(items, slots, 0, 2);

        Player player = Mockito.mock(Player.class);
        PlayerInventory inventory = Mockito.mock(PlayerInventory.class);

        Mockito.when(player.getInventory()).thenReturn(inventory);

        ItemStack helmet = Mockito.mock(ItemStack.class);
        Mockito.when(helmet.getType()).thenReturn(Material.LEATHER_HELMET);
        ItemStack boots = Mockito.mock(ItemStack.class);
        Mockito.when(boots.getType()).thenReturn(Material.LEATHER_CHESTPLATE);
        ItemStack chest = Mockito.mock(ItemStack.class);
        Mockito.when(chest.getType()).thenReturn(Material.LEATHER_CHESTPLATE);
        ItemStack legs = Mockito.mock(ItemStack.class);
        Mockito.when(legs.getType()).thenReturn(Material.DIAMOND_BOOTS);
        ItemStack hand = Mockito.mock(ItemStack.class);
        Mockito.when(hand.getType()).thenReturn(Material.AIR);

        Mockito.when(inventory.getBoots()).thenReturn(null);
        Mockito.when(inventory.getHelmet()).thenReturn(null);
        Mockito.when(inventory.getChestplate()).thenReturn(null);
        Mockito.when(inventory.getLeggings()).thenReturn(null);
        Mockito.when(inventory.getItemInMainHand()).thenReturn(null);
        Mockito.when(inventory.getStorageContents()).thenReturn(null);


        int result = itemBonus.getBonusFromPlayer(player);

        int requiredResult = 2;

        assertEquals(result, requiredResult);
    }

    @Test
    void getBonusFromPlayer3() throws Exception {
        ArrayList<String> items = new ArrayList<>(){{add("LEATHER_HELMET"); add("LEATHER_CHESTPLATE"); add("!diamond_boots"); add("!diamond_leggings");}};
        ArrayList<String> slots = new ArrayList<>(){{add("*HEAD"); add("*FEED"); add("*CHEST"); add("*LEGS");}};
        EquipmentBonus.ItemBonus itemBonus = EquipmentBonus.ItemBonus.generateItemBonus(items, slots, 2, 2);

        Player player = Mockito.mock(Player.class);
        PlayerInventory inventory = Mockito.mock(PlayerInventory.class);

        Mockito.when(player.getInventory()).thenReturn(inventory);

        ItemStack helmet = Mockito.mock(ItemStack.class);
        Mockito.when(helmet.getType()).thenReturn(Material.LEATHER_HELMET);
        ItemStack boots = Mockito.mock(ItemStack.class);
        Mockito.when(boots.getType()).thenReturn(Material.LEATHER_CHESTPLATE);
        ItemStack chest = Mockito.mock(ItemStack.class);
        Mockito.when(chest.getType()).thenReturn(Material.LEATHER_CHESTPLATE);
        ItemStack legs = Mockito.mock(ItemStack.class);
        Mockito.when(legs.getType()).thenReturn(Material.DIAMOND_BOOTS);
        ItemStack hand = Mockito.mock(ItemStack.class);
        Mockito.when(hand.getType()).thenReturn(Material.AIR);

        Mockito.when(inventory.getBoots()).thenReturn(boots);
        Mockito.when(inventory.getHelmet()).thenReturn(helmet);
        Mockito.when(inventory.getChestplate()).thenReturn(chest);
        Mockito.when(inventory.getLeggings()).thenReturn(legs);
        Mockito.when(inventory.getItemInMainHand()).thenReturn(hand);
        Mockito.when(inventory.getStorageContents()).thenReturn(new ItemStack[]{new ItemStack(Material.DIAMOND_HELMET)});


        int result = itemBonus.getBonusFromPlayer(player);

        int requiredResult = 0;

        assertEquals(result, requiredResult);
    }

    @Test
    void getBonusFromPlayer2() throws Exception {
        ArrayList<String> items = new ArrayList<>(){{add("*LEATHER_HELMET"); add("*diamond_helmet"); add("*diamond_boots"); add("!diamond_sword");}};
        ArrayList<String> slots = new ArrayList<>(){{add("HEAD"); add("*FEED"); add("!HAND"); add("inventory");}};
        EquipmentBonus.ItemBonus itemBonus = EquipmentBonus.ItemBonus.generateItemBonus(items, slots, 2, 3);

        Player player = Mockito.mock(Player.class);
        PlayerInventory inventory = Mockito.mock(PlayerInventory.class);

        Mockito.when(player.getInventory()).thenReturn(inventory);

        ItemStack helmet = Mockito.mock(ItemStack.class);
        Mockito.when(helmet.getType()).thenReturn(Material.LEATHER_HELMET);
        ItemStack boots = Mockito.mock(ItemStack.class);
        Mockito.when(boots.getType()).thenReturn(Material.DIAMOND_BOOTS);
        ItemStack hand = Mockito.mock(ItemStack.class);
        Mockito.when(hand.getType()).thenReturn(Material.AIR);

        Mockito.when(inventory.getBoots()).thenReturn(boots);
        Mockito.when(inventory.getHelmet()).thenReturn(helmet);
        Mockito.when(inventory.getItemInMainHand()).thenReturn(hand);
        Mockito.when(inventory.getStorageContents()).thenReturn(new ItemStack[]{new ItemStack(Material.DIAMOND_HELMET)});


        int result = itemBonus.getBonusFromPlayer(player);

        int requiredResult = 3;

        assertEquals(result, requiredResult);
    }

    @Test
    void getBonusFromPlayer1() throws Exception {
        ArrayList<String> items = new ArrayList<>(){{add("*diamond_helmet"); add("*diamond_boots"); add("!diamond_sword");}};
        ArrayList<String> slots = new ArrayList<>(){{add("*HEAD"); add("FEED"); add("!HAND");}};
        EquipmentBonus.ItemBonus itemBonus = EquipmentBonus.ItemBonus.generateItemBonus(items, slots, 2, 3);

        Player player = Mockito.mock(Player.class);
        PlayerInventory inventory = Mockito.mock(PlayerInventory.class);

        Mockito.when(player.getInventory()).thenReturn(inventory);

        ItemStack helmet = Mockito.mock(ItemStack.class);
        Mockito.when(helmet.getType()).thenReturn(Material.DIAMOND_HELMET);
        ItemStack boots = Mockito.mock(ItemStack.class);
        Mockito.when(boots.getType()).thenReturn(Material.DIAMOND_BOOTS);
        ItemStack hand = Mockito.mock(ItemStack.class);
        Mockito.when(hand.getType()).thenReturn(Material.AIR);

        Mockito.when(inventory.getBoots()).thenReturn(boots);
        Mockito.when(inventory.getHelmet()).thenReturn(helmet);
        Mockito.when(inventory.getItemInMainHand()).thenReturn(hand);



        int result = itemBonus.getBonusFromPlayer(player);

        int requiredResult = 3;

        assertEquals(result, requiredResult);
    }

    @Test
    void generateItemBonus() throws Exception {
        ArrayList<String> items = new ArrayList<>(){{add("*diamond_helmet"); add("diamond_boots"); add("!diamond_sword");}};
        ArrayList<String> slots = new ArrayList<>(){{add("*FEED"); add("HEAD"); add("!INVENTORY");}};
        EquipmentBonus.ItemBonus itemBonus = EquipmentBonus.ItemBonus.generateItemBonus(items, slots, 3, 3);

        ArrayList<ImportanceLevelObject<Material>> itemsTest = new ArrayList<>();
        ArrayList<ImportanceLevelObject<EquipmentBonus.EquipmentSlotWrapper>> slotsTest = new ArrayList<>();
        itemsTest.add(new EquipmentBonus.RequiredItem(Material.DIAMOND_HELMET));
        itemsTest.add(new EquipmentBonus.NeutralItem(Material.DIAMOND_BOOTS));
        itemsTest.add(new EquipmentBonus.BanItem(Material.DIAMOND_SWORD));

        slotsTest.add(new EquipmentBonus.RequiredSlot(EquipmentBonus.EquipmentSlotWrapper.FEED));
        slotsTest.add(new EquipmentBonus.NeutralSlot(EquipmentBonus.EquipmentSlotWrapper.HEAD));
        slotsTest.add(new EquipmentBonus.BanSlot(EquipmentBonus.EquipmentSlotWrapper.INVENTORY));

        EquipmentBonus.ItemBonus resultItemBonus = EquipmentBonus.ItemBonus.class.getDeclaredConstructor(ArrayList.class, ArrayList.class, int.class, int.class).newInstance(itemsTest, slotsTest, 3,3);


        assertTrue(itemBonus.equals(resultItemBonus));
    }
}