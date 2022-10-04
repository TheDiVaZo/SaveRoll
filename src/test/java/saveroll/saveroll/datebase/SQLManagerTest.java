package saveroll.saveroll.datebase;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class SQLManagerTest {

    public static SQLManager sqlManager = new SQLManager();
    static {
        sqlManager.setUrl("jdbc:mysql://vh368.timeweb.ru/cl11741_testplug");
        sqlManager.setRootNickname("cl11741_testplug");
        sqlManager.setPassword("7vx4KCBz");
    }

    @org.junit.jupiter.api.Test
    void setAttackRoll() throws SQLException {
        int result = sqlManager.setAttackRoll("User", 100);
        assertEquals(result, 100);
    }

    @org.junit.jupiter.api.Test
    void setDefendRoll() throws SQLException {
        int result = sqlManager.setDefendRoll("User", 200);
        assertEquals(result, 200);
    }

    @org.junit.jupiter.api.Test
    void setEscapeRoll() throws SQLException {
        int result = sqlManager.setEscapeRoll("User", 1);
        assertEquals(result, 1);
    }

    @org.junit.jupiter.api.Test
    void getRollFromUser() {
        int[] result = sqlManager.getRollFromUser("User");
        assertArrayEquals(result, new int[]{0,0,0});
    }

    @org.junit.jupiter.api.Test
    void addUser() {
        sqlManager.createTable();
        sqlManager.addUser("User", 1,2,3);
        assertArrayEquals(new int[1], new int[1]);
    }

    @org.junit.jupiter.api.Test
    void checkUser() {
        boolean result = sqlManager.checkUser("User");
        assertFalse(result);
    }
}