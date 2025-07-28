package com.example.demo;

import java.sql.Connection;
import java.sql.SQLException;
import com.example.demo.config.ConfigManager;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DbUtil {
    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(ConfigManager.get("db.url", System.getenv("PG_URL")));
        config.setUsername(ConfigManager.get("db.user", System.getenv("PG_USER")));
        config.setPassword(ConfigManager.get("db.password", System.getenv("PG_PASSWORD")));
        config.setMaximumPoolSize(10);

        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
