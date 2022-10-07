package saveroll.saveroll.bonus;

import org.bukkit.Material;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import saveroll.logging.JULHandler;
import saveroll.logging.Logger;
import saveroll.saveroll.ImportanceLevelObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@PrepareForTest(EquipmentBonus.EquipmentSlotWrapper.class)
class ItemBonusTest {

    @Test
    void generateItemBonus() throws Exception {
        Logger.init(new JULHandler(java.util.logging.Logger.getAnonymousLogger()));
        ArrayList<String> items = new ArrayList<>(){{add("*diamond_helmet"); add("diamond_boots"); add("!diamond_sword");}};
        ArrayList<String> slots = new ArrayList<>(){{add("*FEED"); add("HEAD"); add("!INVENTORY");}};
        EquipmentBonus.ItemBonus itemBonus = EquipmentBonus.ItemBonus.generateItemBonus(items, slots, 3, 3);

        ArrayList<ImportanceLevelObject<Material>> itemsTest = new ArrayList<>();
        ArrayList<ImportanceLevelObject<EquipmentBonus.EquipmentSlotWrapper>> slotsTest = new ArrayList<>();
        itemsTest.add(new EquipmentBonus.RequiedItem(Material.DIAMOND_HELMET));
        itemsTest.add(new EquipmentBonus.NeutralItem(Material.DIAMOND_BOOTS));
        itemsTest.add(new EquipmentBonus.BanItem(Material.DIAMOND_SWORD));

        slotsTest.add(new EquipmentBonus.RequiedSlot(EquipmentBonus.EquipmentSlotWrapper.FEED));
        slotsTest.add(new EquipmentBonus.NeutralSlot(EquipmentBonus.EquipmentSlotWrapper.HEAD));
        slotsTest.add(new EquipmentBonus.BanSlot(EquipmentBonus.EquipmentSlotWrapper.INVENTORY));

        EquipmentBonus.ItemBonus resultItemBonus = EquipmentBonus.ItemBonus.class.getDeclaredConstructor(ArrayList.class, ArrayList.class, int.class, int.class).newInstance(itemsTest, slotsTest, 3,3);


        assertTrue(itemBonus.equals(resultItemBonus));
    }
}