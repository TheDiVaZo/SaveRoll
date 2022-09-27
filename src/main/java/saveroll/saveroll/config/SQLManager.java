package saveroll.saveroll.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import saveroll.saveroll.datebase.SQL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class SQLManager {
    private String url;
    private String user;
    private String password;
    private HikariDataSource ds;

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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public SQLManager(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public SQLManager(String url, String user, String password,HashMap<String, String> params) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.params = params;
    }

    public SQLManager() {}

    private void createConnect() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(getUrl());
        config.setUsername(getUser());
        config.setPassword(getPassword());
        config.setDriverClassName("com.mysql.jdbc.Driver");
        getParams().forEach(config::addDataSourceProperty);

        ds = new HikariDataSource(config);
    }

    private ResultSet request(String sql) throws SQLException {
        if(ds==null) createConnect();
        try(Connection conn = ds.getConnection()) {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            statement.close();
            return resultSet;
        }
    }
}
