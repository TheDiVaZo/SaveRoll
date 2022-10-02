package saveroll.saveroll.datebase;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class SQLManager {
    private String url;
    private String rootNickname;
    private String password;
    private HikariDataSource ds;

    private static final String tableName = "_rollPlus";
    private static final String tablePlayerName = "playername";

    private static final String tablePlayerCustomRollDefend = "defend";
    private static final String tablePlayerCustomRollAttack = "attack";
    private static final String tablePlayerCustomRollEscape = "escape";

    private HashMap<String, String> params = new HashMap<String, String>(){{
        put("cachePrepStmts", "true");
        put("prepStmtCacheSize", "250");
        put("prepStmtCacheSqlLimit", "2048");}};

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRootNickname() {
        return rootNickname;
    }

    public void setRootNickname(String rootNickname) {
        this.rootNickname = rootNickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }

    public SQLManager(String url, String rootNickname, String password) {
        this.url = url;
        this.rootNickname = rootNickname;
        this.password = password;
    }

    public SQLManager(String url, String rootNickname, String password, HashMap<String, String> params) {
        this.url = url;
        this.rootNickname = rootNickname;
        this.password = password;
        this.params = params;
    }

    public SQLManager() {}

    private void createConnect() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(getUrl());
        config.setUsername(getRootNickname());
        config.setPassword(getPassword());
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        getParams().forEach(config::addDataSourceProperty);

        ds = new HikariDataSource(config);
    }

    private void request(String sql) throws SQLException {
        if(ds==null) createConnect();
        try(Connection conn = ds.getConnection()) {
            Statement statement = conn.createStatement();
            statement.execute(sql);
            statement.close();
        }
    }

    private ResultSet requestQuery(String sql) throws SQLException {
        if(ds==null) createConnect();
        Connection conn = ds.getConnection();
        Statement statement = conn.createStatement();
        return statement.executeQuery(sql);

    }

    public int setRoll(String player, String rollName, int rollNumber) throws SQLException {
        switch (rollName) {
            case "attack": return setAttackRoll(player, rollNumber);
            case "defend": return setDefendRoll(player, rollNumber);
            case "escape": return setEscapeRoll(player, rollNumber);
            default: return 0;
        }

    }

    public int setAttackRoll(String player, int roll) throws SQLException {
        request("UPDATE "+tableName+" SET "+tablePlayerCustomRollAttack+"="+roll+" WHERE "+tablePlayerName+"='"+player+"'");
        return roll;
    }

    public int setDefendRoll(String player, int roll) throws SQLException {
        request("UPDATE "+tableName+" SET "+tablePlayerCustomRollDefend+"="+roll+" WHERE "+tablePlayerName+"='"+player+"'");
        return roll;
    }

    public int setEscapeRoll(String player, int roll) throws SQLException {
        request("UPDATE "+tableName+" SET "+tablePlayerCustomRollEscape+"="+roll+" WHERE "+tablePlayerName+"='"+player+"'");
        return roll;
    }

    public int[] getRollFromUser(String player) {
        try(ResultSet resultSet = requestQuery("SELECT "+tablePlayerCustomRollAttack+", "+tablePlayerCustomRollDefend+", "+tablePlayerCustomRollEscape+" FROM "+tableName+" WHERE "+tablePlayerName+"='"+player+"'")) {
            int[] rolls = new int[3];
            while(resultSet.next()){
                //Retrieve by column name
                rolls[0] = resultSet.getInt(tablePlayerCustomRollAttack);
                rolls[1] = resultSet.getInt(tablePlayerCustomRollDefend);
                rolls[2] = resultSet.getInt(tablePlayerCustomRollEscape);
            }
            return rolls;
        } catch (SQLException e) {
            e.printStackTrace();
            return new int[3];
        }
    }

    public void addUser(String player) {
        addUser(player, 0, 0, 0);
    }

    public void addUser(String player, int attack_roll, int defend_roll, int escape_roll) {
        try {
            request("INSERT IGNORE INTO "+tableName+" ("+tablePlayerName+","+tablePlayerCustomRollAttack+","+tablePlayerCustomRollDefend+","+tablePlayerCustomRollEscape+") VALUES('"+player+"', "+attack_roll+", "+defend_roll+", "+escape_roll+");");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable() {
        try {
            request("CREATE TABLE IF NOT EXISTS "+tableName+"("+tablePlayerName+" varchar(100) primary key,"+tablePlayerCustomRollAttack+" integer, "+tablePlayerCustomRollDefend+" integer, "+tablePlayerCustomRollEscape+" integer);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkUser(String player) {
        try (ResultSet nameOfUser = requestQuery("SELECT * FROM "+tableName+" WHERE "+tablePlayerName+" = '"+player+"'")){
            return nameOfUser.next();
        } catch (SQLException e) {
            return false;
        }
    }
}
